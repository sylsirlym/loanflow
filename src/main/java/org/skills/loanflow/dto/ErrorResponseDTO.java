package org.skills.loanflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 08:05
 */
@Builder
@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private Integer errorCode;
    private String errorMessage;
    private Object errorDetails;
}
