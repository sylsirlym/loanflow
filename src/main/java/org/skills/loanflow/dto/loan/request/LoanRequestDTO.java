package org.skills.loanflow.dto.loan.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 13:36
 */
@Data
public class LoanRequestDTO {
    private BigDecimal requestedAmount;//Requested Amount
    private Integer disbursementInstallments; //No of disbursements. Divide principal with this no
    private int gracePeriodInDays;
}