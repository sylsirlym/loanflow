package org.skills.loanflow.dto.products.request;

import lombok.Data;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 10:46
 */
@Data
public class FeeRequestDTO {
    private Integer feeTypeId;
    private Double amount;
    private String currency;
}
