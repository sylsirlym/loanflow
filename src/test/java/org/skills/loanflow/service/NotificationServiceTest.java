package org.skills.loanflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.dto.notification.NotificationResponseDTO;
import org.skills.loanflow.entity.customer.ProfileEntity;
import org.skills.loanflow.entity.notification.NotificationEntity;
import org.skills.loanflow.entity.notification.NotificationTemplateEntity;
import org.skills.loanflow.enums.NotificationChannels;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 24/03/2025
 * Time: 01:31
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationService notificationService;

    private ProfileEntity profile;
    private NotificationTemplateEntity template;
    private NotificationEntity notification;

    @BeforeEach
    void setUp() {
        profile = new ProfileEntity();
        profile.setMsisdn("254712345678");
        profile.setPreferredNotificationChannel(NotificationChannels.SMS);

        template = new NotificationTemplateEntity();
        template.setTemplateText("Hello {name}, your loan is approved.");

        notification = new NotificationEntity();
        notification.setProfile(profile);
        notification.setMessage("Hello John, your loan is approved.");
    }

    @Test
    @DisplayName("Test Generate Message")
    void testGenerateMessage() {
        when(storageService.findByEventTypeAndChannel("LOAN_APPROVED", "SMS"))
                .thenReturn(template);

        String message = notificationService.generateMessage("LOAN_APPROVED", "SMS", Map.of("name", "John"));

        assertEquals("Hello John, your loan is approved.", message);
    }

    @Test
    @DisplayName("Test Fetch Notifications")
    void testFetchNotifications() {
        var notification = new NotificationResponseDTO();
        notification.setMessage("Hello John, your loan is approved.");
        var notificationEntity = new NotificationEntity();
        when(storageService.findNotificationsByMsisdn("254712345678"))
                .thenReturn(List.of(notificationEntity));

        when(modelMapper.map(any(NotificationEntity.class), eq(NotificationResponseDTO.class)))
                .thenReturn(notification);

        List<NotificationResponseDTO> notifications = notificationService.fetchNotifications("254712345678");

        assertEquals(1, notifications.size());
        assertEquals("Hello John, your loan is approved.", notifications.get(0).getMessage());
    }
}
