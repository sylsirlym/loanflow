package org.skills.loanflow.eventmanagement;

import lombok.extern.slf4j.Slf4j;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.loan.RepaymentScheduleEntity;
import org.skills.loanflow.service.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 19:45
 */
@Slf4j
@Component
public class LoanEventListener {

    private final NotificationService notificationService;

    public LoanEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleLoanEvent(LoanEvent event) {
        LoanEntity loan = event.getLoan();
        var variables = this.generateNotificationMap(event.getEventType(), loan);
        notificationService.sendNotification(loan.getLoanOffer().getProfile(), variables, event.getEventType());
    }

    private Map<String, String> generateNotificationMap(String eventType, LoanEntity loan) {
        Map<String, String> variables = new HashMap<>();

        switch (eventType) {
            case "LOAN_CREATED" -> variables.put("amount", loan.getPrincipal().toString());
            case "DUE_REMINDER" -> {
                var nextSchedule = getNextRepaymentSchedule(loan.getRepaymentSchedules());
                nextSchedule.ifPresent(schedule -> {
                    variables.put("amount", schedule.getInstallmentAmount().toString());
                    variables.put("due_date", schedule.getDueDate().toString());
                });
            }
            case "OVERDUE" -> variables.put("product_name", loan.getLoanOffer().getProduct().getName());
            default -> log.info("Event type:{} not known", eventType);
        }

        return variables;
    }




    public Optional<RepaymentScheduleEntity> getNextRepaymentSchedule(List<RepaymentScheduleEntity> repaymentSchedules) {
        LocalDate today = LocalDate.now();

        return repaymentSchedules.stream()
                .filter(schedule -> schedule.getDueDate().isAfter(today)) // Only future due dates
                .min(Comparator.comparing(RepaymentScheduleEntity::getDueDate)); // Get the closest due date
    }
}

