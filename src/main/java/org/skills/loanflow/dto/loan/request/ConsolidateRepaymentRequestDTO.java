package org.skills.loanflow.dto.loan.request;

import lombok.Data;

import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 12:53
 */
@Data
public class ConsolidateRepaymentRequestDTO {
    private List<Long> loanIds;
    private int newRepaymentDay;
}
