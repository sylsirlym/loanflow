package org.skills.loanflow.dto.products;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 21:38
 */
@Data
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private int tenure;
    private BigDecimal serviceFee;
    private BigDecimal dailyFee;
    private BigDecimal lateFee;
    private int daysAfterDueForFeeApplication;
}
