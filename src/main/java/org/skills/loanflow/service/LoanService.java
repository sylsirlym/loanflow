package org.skills.loanflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skills.loanflow.configs.LoanFlowConfigs;
import org.skills.loanflow.dto.loan.request.LoanRequestDTO;
import org.skills.loanflow.dto.loan.response.LoanResponseDTO;
import org.skills.loanflow.entity.loan.DisbursementEntity;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.product.FeeTypeEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.skills.loanflow.entity.product.ProductFeeEntity;
import org.skills.loanflow.enums.LoanState;
import org.skills.loanflow.exception.LoanException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 10:29
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {
    private final StorageService storageService;
    private final LoanFlowConfigs configs;

    /**
     * Create a new loan.
     */
    @Transactional
    public LoanResponseDTO requestLoan(Long loanOfferId,LoanRequestDTO loanRequestDTO) {
        var loanOffer = storageService.findLoanOfferByID(loanOfferId);
        var product = loanOffer.getProduct();
        var gracePeriodInDays = loanRequestDTO.getGracePeriodInDays();
        var loanEntity = new LoanEntity();
        loanEntity.setLoanOffer(loanOffer);
        var requestedAmount = loanRequestDTO.getRequestedAmount();
        var loanLimit = loanOffer.getLoanLimit();
        if(requestedAmount.compareTo(loanLimit) > 0){
            throw new LoanException("The requested amount "+requestedAmount+" is greater than the loan limit "+loanLimit+".");
        }
        loanEntity.setPrincipal(loanRequestDTO.getRequestedAmount());
        var netDisbursedAmount = this.applyBeforeDisbursementFeesToLoan(requestedAmount, loanOffer.getProduct());
        loanEntity.setNetDisbursedAmount(netDisbursedAmount);
        loanEntity.setGracePeriodInDays(gracePeriodInDays);

        loanEntity.setDisbursementDate(new Date());
        loanEntity.setLoanState(LoanState.OPEN.name());
        if(product.getDisbursementType().equalsIgnoreCase(configs.getLumpSumpDisbursementType())) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, gracePeriodInDays);
            loanEntity.setDueDate(calendar.getTime());
        }
         var savedLoan =storageService.createLoan(loanEntity);
        var disbursedAmount = this.getNetDisbursementAmount(product,loanRequestDTO, netDisbursedAmount);
        var savedDisbursement = this.disburseLoan(savedLoan,disbursedAmount);

        var loanResponse = new LoanResponseDTO();
        loanResponse.setLoanName(savedLoan.getLoanOffer().getProduct().getName());
        loanResponse.setPrincipal(savedLoan.getPrincipal());
        loanResponse.setPrincipalDisbursed(netDisbursedAmount);
        loanResponse.setDisbursementDate(savedDisbursement.getDateCreated());
        loanResponse.setLoanState(loanEntity.getLoanState());
        return loanResponse;
    }

    private BigDecimal getNetDisbursementAmount(ProductEntity productEntity, LoanRequestDTO loanRequestDTO, BigDecimal netDisbursedAmount) {
        if(productEntity.getDisbursementType().equalsIgnoreCase(configs.getTrancheBasedDisbursementType())){
            log.info("This is loan will be disbursed in installments");
            return netDisbursedAmount.divide(BigDecimal.valueOf(loanRequestDTO.getDisbursementInstallments()), 2, RoundingMode.HALF_UP);
        }
        log.info("This is loan will be disbursed in lump sum");
        return netDisbursedAmount;
    }

    /**
     * Calculate and apply any to be applied before disbursement.
     * This gives us the Net Disbursement Amount
     */
    private BigDecimal applyBeforeDisbursementFeesToLoan(BigDecimal requestedAmount, ProductEntity product) {
        BigDecimal totalFee = BigDecimal.ZERO;

        for (ProductFeeEntity productFee : product.getProductFees()) {
            FeeTypeEntity feeTypeEntity = productFee.getFeeTypeEntity();

            if (configs.getChargeBefore().equals(feeTypeEntity.getWhenToCharge())) {
                if (configs.getFixedAmountLoanType().equals(feeTypeEntity.getFeeType())) {
                    totalFee = totalFee.add(BigDecimal.valueOf(productFee.getFeeAmount()));
                } else if (configs.getPercentageOnAmountLoanType().equals(feeTypeEntity.getFeeType())) {
                    BigDecimal percentageFee = requestedAmount.multiply(BigDecimal.valueOf(productFee.getFeeRate() / 100.0));
                    totalFee = totalFee.add(percentageFee);
                }
            }
        }
        return requestedAmount.min(totalFee);
    }

    private DisbursementEntity disburseLoan(LoanEntity loanEntity, BigDecimal disbursementAmount) {
        var disbursementEntity = new DisbursementEntity();
        disbursementEntity.setLoanEntity(loanEntity);
        disbursementEntity.setAmount(disbursementAmount);
        disbursementEntity.setDateCreated(new Date());
        return storageService.saveDisbursement(disbursementEntity);
    }
}
