package org.skills.loanflow.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 06:09
 */
@Component
@Data
@ConfigurationProperties("config.loanflow")
public class LoanFlowConfigs {
    private String fixedAmountLoanType;
    private String percentageOnAmountLoanType;
    private String percentageOnBalanceLoanType;
    private String lumpSumpDisbursementType;
    private String trancheBasedDisbursementType;
    private String chargeBefore;
    private String chargeDuring;
    private String chargeAfter;
}
