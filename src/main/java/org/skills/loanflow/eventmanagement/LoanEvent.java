package org.skills.loanflow.eventmanagement;

import lombok.Getter;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.springframework.context.ApplicationEvent;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 19:42
 */
@Getter
public class LoanEvent extends ApplicationEvent {
    private final LoanEntity loan;
    private final String eventType;

    public LoanEvent(Object source, LoanEntity loan, String eventType) {
        super(source);
        this.loan = loan;
        this.eventType = eventType;
    }
}
