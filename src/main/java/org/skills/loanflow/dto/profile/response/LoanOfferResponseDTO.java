package org.skills.loanflow.dto.profile.response;

import lombok.Data;
import org.skills.loanflow.dto.product.response.ProductResponseDTO;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 00:04
 */
@Data
public class LoanOfferResponseDTO {
    private Long loanOfferId;
    private ProductResponseDTO product;
    private BigDecimal loanLimit;
}
