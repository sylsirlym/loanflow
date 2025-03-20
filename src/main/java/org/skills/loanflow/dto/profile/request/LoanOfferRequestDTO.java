package org.skills.loanflow.dto.profile.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 01:00
 */
@Data
public class LoanOfferRequestDTO {
    private Long productId;
    private BigDecimal loanLimit;
}
