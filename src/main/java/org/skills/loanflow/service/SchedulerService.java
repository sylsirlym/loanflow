package org.skills.loanflow.service;

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
//    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void triggerDisbursementProcessing() {
        log.info("About to trigger disbursement processing");
        disbursementService.processDueDisbursements();
    }
}
