package org.skills.loanflow.entity.notification;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 21:58
 */
@Data
@Entity
@Table(name = "notification_templates")
@SQLRestriction("active='1'")
public class NotificationTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationTemplateId;

    @Column(nullable = false, unique = true)
    private String eventType; // LOAN_CREATED, DUE_REMINDER, etc.

    @Column(nullable = false)
    private String channel; // SMS, EMAIL, PUSH

    @Column(nullable = false, length = 1000)
    private String templateText; // "Hello {name}, your loan of {amount} is due on {due_date}."

    private Integer active = 1;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    private int createdBy;

    @UpdateTimestamp
    private LocalDateTime dateModified;

    private int modifiedBy;
}
