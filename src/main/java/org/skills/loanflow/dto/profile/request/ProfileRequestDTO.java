package org.skills.loanflow.dto.profile.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.skills.loanflow.enums.NotificationChannels;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 22:40
 */
@Data
public class ProfileRequestDTO {
    @NotBlank(message = "MSISDN is required")
    private String msisdn;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    private String pinHash;
    private String deviceId;
    @Enumerated(EnumType.STRING)
    private NotificationChannels preferredNotificationChannel;

    @DecimalMin(value = "0.0", message = "Credit score cannot be negative")
    private BigDecimal creditScore;
}
