package org.skills.loanflow.dto.loan.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 14:16
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LoanResponseDTO {
    private String loanName;
    private BigDecimal principal;
    private BigDecimal principalDisbursed;
    private Date disbursementDate;
    private int gracePeriodInDays;
    private Date dueDate;
    private String loanState;
}
