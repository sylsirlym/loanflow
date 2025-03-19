package org.skills.loanflow.dto.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

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

    @DecimalMin(value = "0.0", message = "Service fee cannot be negative")
    private BigDecimal serviceFee;

    @DecimalMin(value = "0.0", message = "Daily fee cannot be negative")
    private BigDecimal dailyFee;

    @DecimalMin(value = "0.0", message = "Late fee cannot be negative")
    private BigDecimal lateFee;

    @Min(value = 0, message = "Days after due for fee application cannot be negative")
    private int daysAfterDueForFeeApplication;
}
