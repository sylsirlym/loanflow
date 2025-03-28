package org.skills.loanflow.eventmanagement;

import lombok.extern.slf4j.Slf4j;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 19:44
 */
@Component
@Slf4j
public class LoanEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public LoanEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishLoanEvent(LoanEntity loan, String eventType) {
        log.info("Publishing an event:{}",eventType);
        eventPublisher.publishEvent(new LoanEvent(this, loan, eventType));
    }
}

