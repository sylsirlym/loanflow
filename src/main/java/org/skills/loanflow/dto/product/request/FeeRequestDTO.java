package org.skills.loanflow.dto.product.request;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 10:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeRequestDTO {
    private Integer feeTypeId;
    private Double amount;
    @Max(value = 100, message = "Percentage cannot be greater than 100")
    private Double rate;
}
