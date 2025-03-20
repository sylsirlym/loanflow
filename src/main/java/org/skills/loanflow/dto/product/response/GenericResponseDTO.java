package org.skills.loanflow.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 23:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GenericResponseDTO {
    private String id;
    private String name;
}
