package org.skills.loanflow.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 11:53
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeeTypeResponseDTO {
    private String id;
    private String name;
    private String type;
}
