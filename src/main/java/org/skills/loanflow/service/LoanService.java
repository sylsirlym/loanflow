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
import org.skills.loanflow.enums.DisbursementStatus;
import org.skills.loanflow.enums.LoanState;
import org.skills.loanflow.exception.LoanException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        loanEntity.setLoanState(LoanState.OPEN.name());
        var disbursements = this.generateLoanDisbursements(product.getDisbursementType(),loanRequestDTO.getDisbursementInstallments(), netDisbursedAmount, loanEntity);
        loanEntity.setDisbursements(disbursements);
        var savedLoan = storageService.saveLoan(loanEntity);

        var loanResponse = new LoanResponseDTO();
        loanResponse.setLoanName(savedLoan.getLoanOffer().getProduct().getName());
        loanResponse.setPrincipal(savedLoan.getPrincipal());
        loanResponse.setPrincipalDisbursed(netDisbursedAmount);
        loanResponse.setLoanState(loanEntity.getLoanState());
        return loanResponse;
    }

    private List<DisbursementEntity> generateLoanDisbursements(String disbursementType, Integer installments, BigDecimal netDisbursedAmount, LoanEntity loan) {
        var disbursements = new ArrayList<DisbursementEntity>();

        if (disbursementType.equalsIgnoreCase(configs.getTrancheBasedDisbursementType())) {
            log.info("This loan will be disbursed in installments");
            BigDecimal trancheAmount = netDisbursedAmount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
            LocalDate startDate =  LocalDate.now();

            for (int i = 0; i < installments; i++) {
                var disbursement = new DisbursementEntity();
                disbursement.setLoanEntity(loan);
                disbursement.setAmount(trancheAmount);
                disbursement.setScheduledDate(startDate.plusMonths(i));
                disbursement.setDisbursed(false);
                disbursement.setDisbursementStatus(DisbursementStatus.PENDING);

                disbursements.add(disbursement);
            }
        } else {
            log.info("This loan will be disbursed in lump sum");
            var disbursement = new DisbursementEntity();
            disbursement.setLoanEntity(loan);
            disbursement.setAmount(netDisbursedAmount);
            disbursement.setScheduledDate(LocalDate.now());
            disbursement.setDisbursed(false);
            disbursement.setDisbursementStatus(DisbursementStatus.PENDING);

            disbursements.add(disbursement);
        }

        return disbursements;
    }

    /**
     * Calculate and apply any to be applied before disbursement.
     * This gives us the Net Disbursement Amount
     */
    private BigDecimal applyBeforeDisbursementFeesToLoan(BigDecimal requestedAmount, ProductEntity product) {
        var totalFee = BigDecimal.ZERO;

        for (ProductFeeEntity productFee : product.getProductFees()) {
            FeeTypeEntity feeTypeEntity = productFee.getFeeTypeEntity();
            var feeType = feeTypeEntity.getFeeType();
            log.info("Loan fee type is {}", feeType);
            if (configs.getChargeBefore().equals(feeTypeEntity.getWhenToCharge())) {
                if (configs.getFixedAmountLoanType().equals(feeType)) {
                    totalFee = totalFee.add(BigDecimal.valueOf(productFee.getFeeAmount()));
                } else if (configs.getPercentageOnAmountLoanType().equals(feeType)) {
                    BigDecimal percentageFee = requestedAmount.multiply(BigDecimal.valueOf(productFee.getFeeRate() / 100.0));
                    log.info("Percentage fee is {}", percentageFee);
                    totalFee = totalFee.add(percentageFee);
                }
            }
            log.info("Total fee is {}",totalFee);
        }
        return requestedAmount.subtract(totalFee);
    }
}
