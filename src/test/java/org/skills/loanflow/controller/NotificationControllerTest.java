package org.skills.loanflow.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skills.loanflow.dto.notification.NotificationResponseDTO;
import org.skills.loanflow.service.NotificationService;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 24/03/2025
 * Time: 01:29
 */
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void testGetNotifications() {
        var notification = new NotificationResponseDTO();

        when(notificationService.fetchNotifications("254712345678"))
                .thenReturn(List.of(notification));

        ResponseEntity<Object> response = notificationController.getNotifications("254712345678");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
