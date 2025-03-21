package org.skills.loanflow.dto.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 21:34
 */
@Data
public class ProductRequestDTO {
    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 1, message = "Tenure must be at least 1 day/month")
    private int tenureDuration;
    private int tenureDurationTypeID;
    private String disbursementTypes;
    private int disbursementIntervalInDays;
    @Min(value = 0, message = "Days after due for fee application cannot be negative")
    private int daysAfterDueForFeeApplication;
    private List<FeeRequestDTO> fees;
}
