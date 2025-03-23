package org.skills.loanflow.eventmanagement;

import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.loan.RepaymentScheduleEntity;
import org.skills.loanflow.service.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 19:45
 */
@Component
public class LoanEventListener {

    private final NotificationService notificationService;

    public LoanEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleLoanEvent(LoanEvent event) {
        LoanEntity loan = event.getLoan();
        String message = this.generateMessage(event.getEventType(), loan);
        notificationService.sendNotification(loan.getLoanOffer().getProfile(), message, event.getEventType());
    }

    private String generateMessage(String eventType, LoanEntity loan) {
        return switch (eventType) {
            case "LOAN_CREATED" -> "Your loan of " + loan.getPrincipal() + " has been received.";
            case "DUE_REMINDER" -> {
                var nextSchedule = getNextRepaymentSchedule(loan.getRepaymentSchedules());
                yield nextSchedule.map(schedule ->
                                "Reminder: Your loan payment of " + schedule.getInstallmentAmount() + " is due on " + schedule.getDueDate() + ".")
                        .orElse("Reminder: You have no upcoming due payments.");
            }
            case "OVERDUE" -> "Alert: Your loan is overdue. Please make payment immediately.";
            default -> "Notification regarding your loan.";
        };
    }


    public Optional<RepaymentScheduleEntity> getNextRepaymentSchedule(List<RepaymentScheduleEntity> repaymentSchedules) {
        LocalDate today = LocalDate.now();

        return repaymentSchedules.stream()
                .filter(schedule -> schedule.getDueDate().isAfter(today)) // Only future due dates
                .min(Comparator.comparing(RepaymentScheduleEntity::getDueDate)); // Get the closest due date
    }
}

