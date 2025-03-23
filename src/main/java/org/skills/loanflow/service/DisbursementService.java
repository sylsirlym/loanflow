package org.skills.loanflow.service;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 22/03/2025
 * Time: 15:03
 */

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skills.loanflow.configs.LoanFlowConfigs;
import org.skills.loanflow.entity.loan.DisbursementEntity;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.loan.RepaymentScheduleEntity;
import org.skills.loanflow.enums.BillingCycle;
import org.skills.loanflow.enums.DisbursementStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DisbursementService {
    private final StorageService storageService;
    private final NotificationService notificationService;
    private final LoanFlowConfigs configs;

    @Transactional
    public void processDueDisbursements() {
        var dueDisbursements = storageService.findUnprocessedDisbursements();

        for (DisbursementEntity disbursement : dueDisbursements) {
            var loan = disbursement.getLoanEntity();

            // Process disbursement
            disbursement.setDisbursed(true);
            disbursement.setTransactionReference(UUID.randomUUID().toString());
            disbursement.setDisbursementStatus(DisbursementStatus.DISBURSED);
            disbursement.setActualDisbursementDate(LocalDate.now());
            storageService.saveDisbursement(disbursement);

            // Check if all tranches are disbursed
            var allDisbursed = loan.getDisbursements().stream().allMatch(DisbursementEntity::isDisbursed);
            if (allDisbursed) {
                loan.setFullyDisbursed(true);
                var product = loan.getLoanOffer().getProduct();
                var date = calculateDueDate(LocalDate.now(), product.getTenureDuration(), product.getTenureDurationTypeEntity().getTenureDurationType(),loan.getGracePeriodInDays());
                loan.setDueDate(date);
                storageService.saveLoan(loan);
                if (product.getBillingCycle().equals(BillingCycle.MONTHLY)) {
                    this.generateRepaymentSchedule(loan);
                }
            }

            // Send notifications
            sendDisbursementNotification(loan, disbursement);

            log.info("Disbursed tranche: {} for loan: {}", disbursement.getAmount(),loan.getLoanId());
        }
    }

    private void sendDisbursementNotification(LoanEntity loan, DisbursementEntity disbursement) {
        String subject = "Loan Disbursement Alert";
        String message = "Dear Customer,\n\nYour disbursement of " + disbursement.getAmount() +
                " has been disbursed on " + LocalDate.now() + ".\n\nThank you for banking with us.";

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

    private LocalDate calculateDueDate(LocalDate date, int tenure, String tenureUnit, int gracePeriod) {
        // Apply grace period
        date = date.plusDays(gracePeriod);

        // Apply tenure
        if (tenureUnit.equalsIgnoreCase(configs.getTenureDurationTypeDay())) {
            return date.plusDays(tenure);
        } else if (tenureUnit.equalsIgnoreCase(configs.getTenureDurationTypeMonth())) {
            return date.plusMonths(tenure);
        }  else if (tenureUnit.equalsIgnoreCase(configs.getTenureDurationTypeYear())) {
            return date.plusYears(tenure);
        } else {
            throw new IllegalArgumentException("Invalid tenure unit. Use 'days' or 'months'.");
        }
    }

    private void generateRepaymentSchedule(LoanEntity loan) {
        var product = loan.getLoanOffer().getProduct();
        var tenure = product.getTenureDuration();

        // Convert years to months if tenure is in years
        if (product.getTenureDurationTypeEntity().getTenureDurationType().equalsIgnoreCase(configs.getTenureDurationTypeYear())) {
            tenure *= 12; // Convert years to months
        }

        var monthlyInstallment = loan.getPrincipal().divide(BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

        for (int i = 0; i < tenure; i++) {
            RepaymentScheduleEntity repayment = RepaymentScheduleEntity.builder()
                    .loan(loan)
                    .dueDate(loan.getDueDate().plusMonths(i))
                    .installmentAmount(monthlyInstallment)
                    .paid(false)
                    .build();
            storageService.saveRepayment(repayment);
        }
    }

}

