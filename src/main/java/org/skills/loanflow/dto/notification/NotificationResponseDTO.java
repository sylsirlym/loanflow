package org.skills.loanflow.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 21:12
 */
@Data
public class NotificationResponseDTO {
    private String message;
    private String channel;
    private LocalDateTime dateSend;
    private boolean delivered;
    private String eventType;
}
