package org.skills.loanflow.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 22/03/2025
 * Time: 17:12
 */
@Component
@AllArgsConstructor
@Slf4j
public class SchedulerService {
    private final DisbursementService disbursementService;
    private final LoanService loanService;
//    @Scheduled(cron = "0 0 8 * * ?")//Runs every day at 8 a.m
    @Scheduled(cron = "0 */5 * * * ?")
    public void triggerDisbursementProcessing() {
        log.info("About to trigger disbursement processing");
        disbursementService.processDueDisbursements();
    }

//    @Scheduled(cron = "0 0 0 * * ?")//Runs at midnight
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void checkAndUpdateLoanStates(){
        log.info("About to check and update loan states");
        loanService.checkAndUpdateLoanStates();
    }

//    @Scheduled(cron = "0 0 0 * * ?")//Runs at midnight
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void processOverdueLoans(){
        log.info("About to process overdue loans");
        loanService.processOverdueLoans();
    }

//    @Scheduled(cron = "0 0 9 * * ?") // Run daily at 9 AM
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void processDueDateReminders() {
        log.info("About to run due date reminder job...");
        loanService.sendDueDateReminder();
    }

}
