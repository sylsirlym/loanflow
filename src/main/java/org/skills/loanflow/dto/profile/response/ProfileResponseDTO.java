package org.skills.loanflow.dto.profile.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 22:42
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileResponseDTO {
    private Long profileId;
    private String msisdn;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private int pinStatus;
    private BigDecimal creditScore;
}
