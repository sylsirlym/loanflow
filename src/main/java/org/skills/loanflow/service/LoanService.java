package org.skills.loanflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.configs.LoanFlowConfigs;
import org.skills.loanflow.dto.loan.request.LoanRequestDTO;
import org.skills.loanflow.dto.loan.response.LoanResponseDTO;
import org.skills.loanflow.entity.loan.DisbursementEntity;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.loan.RepaymentScheduleEntity;
import org.skills.loanflow.entity.product.FeeTypeEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.skills.loanflow.entity.product.ProductFeeEntity;
import org.skills.loanflow.enums.BillingCycle;
import org.skills.loanflow.enums.DisbursementStatus;
import org.skills.loanflow.enums.LoanState;
import org.skills.loanflow.exception.LoanException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    /**
     * Create a new loan.
     */
    @Transactional
    public LoanResponseDTO requestLoan(Long loanOfferId,LoanRequestDTO loanRequestDTO) {
        var loanOffer = storageService.findLoanOfferByID(loanOfferId);
        var product = loanOffer.getProduct();
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
        loanEntity.setGracePeriodInDays(loanRequestDTO.getGracePeriodInDays());
        loanEntity.setCoolingOffPeriodInDays(loanRequestDTO.getCoolingOffPeriodInDays());

        loanEntity.setLoanState(LoanState.OPEN);
        var disbursements = this.generateLoanDisbursements(product.getDisbursementType(),loanRequestDTO.getDisbursementInstallments(), netDisbursedAmount, loanEntity);
        loanEntity.setDisbursements(disbursements);
        var savedLoan = storageService.saveLoan(loanEntity);
        return modelMapper.map(savedLoan,LoanResponseDTO.class);
    }

    private List<DisbursementEntity> generateLoanDisbursements(String disbursementType, Integer installments, BigDecimal netDisbursedAmount, LoanEntity loan) {
        var disbursements = new ArrayList<DisbursementEntity>();

        if (disbursementType.equalsIgnoreCase(configs.getTrancheBasedDisbursementType())) {
            log.info("This loan will be disbursed in installments");
            BigDecimal trancheAmount = netDisbursedAmount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
            LocalDate startDate =  LocalDate.now();

            for (int i = 0; i < installments; i++) {
                var disbursement = new DisbursementEntity();
                disbursement.setLoan(loan);
                disbursement.setAmount(trancheAmount);
                disbursement.setScheduledDate(startDate.plusMonths(i));
                disbursement.setDisbursed(false);
                disbursement.setDisbursementStatus(DisbursementStatus.PENDING);

                disbursements.add(disbursement);
            }
        } else {
            log.info("This loan will be disbursed in lump sum");
            var disbursement = new DisbursementEntity();
            disbursement.setLoan(loan);
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

    public List<LoanResponseDTO> fetchLoans(String msisdn,String state){
        var loans = storageService.findAllLoansPerCustomer(msisdn);
        return loans.stream()
                .filter(loan -> state == null || state.isBlank() || loan.getLoanState().equals(LoanState.valueOf(state)))
                .map(loan -> modelMapper.map(loan, LoanResponseDTO.class))
                .toList();
    }

    @Transactional
    public void consolidateRepaymentDate(List<Long> loanIds, int newRepaymentDay) {
        List<LoanEntity> loans = storageService.findLoansByIds(loanIds);

        for (LoanEntity loan : loans) {
            if (!loan.getLoanOffer().getProduct().getBillingCycle().equals(BillingCycle.MONTHLY)) {
                throw new IllegalArgumentException("Only monthly repayment loans can be consolidated.");
            }

            loan.setDueDate(this.adjustDueDate(loan.getDueDate(),newRepaymentDay));
            storageService.saveLoan(loan);

            // Update existing repayment schedules
            var schedules = storageService.findRepaymentsByLoan(loan);
            schedules.forEach(schedule -> schedule.setDueDate(this.adjustDueDate(schedule.getDueDate(),newRepaymentDay)));
            storageService.saveRepayments(schedules);
        }
    }

    private LocalDate adjustDueDate(LocalDate currentDueDate, int newRepaymentDay) {
        // Ensure valid day range (1-28 to avoid February issues)
        int safeRepaymentDay = Math.min(newRepaymentDay, 28);

        return currentDueDate.withDayOfMonth(safeRepaymentDay);
    }

    @Transactional
    public void checkAndUpdateLoanStates() {
        var activeLoans = storageService.findAllLoansByState(LoanState.OPEN);

        for (LoanEntity loan : activeLoans) {
            updateLoanState(loan);

            // Check if loan should be written off
            if (loan.getLoanState() == LoanState.OVERDUE) {
                long monthsOverdue = ChronoUnit.MONTHS.between(loan.getDueDate(), LocalDate.now());
                if (monthsOverdue >= configs.getDelinquencyPeriodInMonths()) {
                    loan.setLoanState(LoanState.WRITTEN_OFF);
                    storageService.saveLoan(loan);
                }
            }
        }
    }

    @Transactional
    public void updateLoanState(LoanEntity loan) {
        if (loan.getLoanState() == LoanState.CANCELLED || loan.getLoanState() == LoanState.CLOSED) {
            return; // No updates needed for already cancelled/closed loans
        }

        if (!loan.isFullyDisbursed()) {
            boolean hasDisbursements = loan.getDisbursements().stream().anyMatch(DisbursementEntity::isDisbursed);
            LocalDate disbursementDeadline = loan.getDateCreated().plusDays(loan.getCoolingOffPeriodInDays());

            if (!hasDisbursements && LocalDate.now().isAfter(disbursementDeadline)) {
                // No disbursements and disbursement period expired → Cancel the loan
                loan.setLoanState(LoanState.CANCELLED);
            }
        } else {
            // Check repayments
            var repayments = loan.getRepaymentSchedules();

            boolean allPaid = repayments.stream().allMatch(RepaymentScheduleEntity::isPaid);
            boolean hasOverdue = repayments.stream()
                    .anyMatch(r -> !r.isPaid() && r.getDueDate().isBefore(LocalDate.now()));

            if (allPaid) {
                loan.setLoanState(LoanState.CLOSED);
            } else if (hasOverdue) {
                loan.setLoanState(LoanState.OVERDUE);
            }
        }
        storageService.saveLoan(loan);
    }

    public void processOverdueLoans(){
        log.info("Start overdue loan processing");
        var overDueLoans = storageService.findAllLoansByState(LoanState.OVERDUE);
        log.info("Fetched {} overdue loans", overDueLoans.size());
        for (LoanEntity loan : overDueLoans) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());

            // Apply late fees
            this.applyLateFees(loan, daysOverdue);
            // Send notifications
            this.sendOverdueNotification(loan);
        }
    }

    private void applyLateFees(LoanEntity loan, long daysOverdue) {
        log.info("Applying late fees on Loan ID {} which {} days overdue", loan.getLoanId(), daysOverdue);
    }

    private void sendOverdueNotification(LoanEntity loan) {
        String subject = "Overdue Loan Alert";
        String message = "Dear Customer,\n\nYour " + loan.getLoanOffer().getProduct().getName() +
                " loan is overdue. \n\nThank you for banking with us.";

        // Send Email Notification
        var email = loan.getLoanOffer().getProfile().getCustomer().getEmail();
        if (email != null) {
            notificationService.sendEmail(email, subject, message);
        }

        // Send SMS Notification
        var msisdn = loan.getLoanOffer().getProfile().getMsisdn();
        if (msisdn != null) {
            notificationService.sendSms(msisdn, message);
        }
    }

    public void cancelLoan(Long loanId) {
        var loan = storageService.findLoanById(loanId);
        if (!loan.isFullyDisbursed()) {
            loan.setLoanState(LoanState.CANCELLED);
            storageService.saveLoan(loan);
        }
    }
}
