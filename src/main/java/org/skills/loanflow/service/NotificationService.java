package org.skills.loanflow.service;

import org.springframework.stereotype.Service;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 22/03/2025
 * Time: 17:09
 */
@Service
public class NotificationService {
    public void sendEmail(String to, String subject, String message) {
        // Simulate email sending (replace with real email service like JavaMailSender)
    }

    public void sendSms(String phoneNumber, String message) {
        // Simulate SMS sending (replace with real SMS gateway like Twilio)
    }
}
