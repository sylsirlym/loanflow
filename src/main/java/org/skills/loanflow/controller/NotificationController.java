package org.skills.loanflow.controller;

import lombok.RequiredArgsConstructor;
import org.skills.loanflow.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 21:03
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{msisdn}")
    public ResponseEntity<Object> getNotifications(@PathVariable String msisdn) {
        var notifications = notificationService.fetchNotifications(msisdn);
        return ResponseEntity.ok(notifications);
    }
}
