package org.skills.loanflow.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.dto.notification.NotificationResponseDTO;
import org.skills.loanflow.entity.customer.ProfileEntity;
import org.skills.loanflow.entity.notification.NotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 22/03/2025
 * Time: 17:09
 */
@AllArgsConstructor
@Service
@Slf4j
public class NotificationService {
    private final StorageService storageService;
    private final ModelMapper modelMapper;

    public void sendNotification(ProfileEntity profile, String message, String eventType) {
        String preferredChannel = profile.getPreferredNotificationChannel().name();
        log.info("Sending {} notification to {}", eventType, profile.getMsisdn());

        NotificationEntity notification = new NotificationEntity();
        notification.setProfile(profile);
        notification.setMessage(message);
        notification.setChannel(preferredChannel);
        notification.setEventType(eventType);

        storageService.saveNotification(notification);

        switch (preferredChannel) {
            case "SMS" -> this.sendSms(profile.getMsisdn(), message);
            case "EMAIL" -> this.sendEmail(profile.getCustomer().getEmail(), "Loan Notification", message);
            case "PUSH" -> this.sendPushNotification(profile.getDeviceId(), message);
            default -> log.warn("Unsupported notification channel: {}", preferredChannel);
        }
    }

    public void sendEmail(String to, String subject, String message) {
        // Simulate email sending
        log.info("Sending Email to:[{}] with subject [{}] and body [{}]", to, subject, message);
    }

    public void sendSms(String phoneNumber, String message) {
        // Simulate SMS sending
        log.info("Sending SMS to:[{}] with message [{}]", phoneNumber, message);
    }

    public void sendPushNotification(String deviceID, String message) {
        // Simulate Push sending
        log.info("Sending Push Notification to:[{}] with message [{}]", deviceID, message);
    }


    public List<NotificationResponseDTO> fetchNotifications(String msisdn) {
        log.info("About to fetch notifications for {}",msisdn);
        var notifications = storageService.findNotificationsByMsisdn(msisdn);
        return notifications.stream()
                .map(notification-> modelMapper.map(notification, NotificationResponseDTO.class))
                .toList();
    }
}
