package org.skills.loanflow.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 21:38
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private String tenure;
    private int daysAfterDueForFeeApplication;
    private String disbursementType;
    private int disbursementIntervalInDays;
    private List<FeeResponseDTO> fees;
}
