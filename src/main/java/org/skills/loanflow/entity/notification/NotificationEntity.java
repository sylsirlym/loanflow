package org.skills.loanflow.entity.notification;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.skills.loanflow.entity.customer.ProfileEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 23/03/2025
 * Time: 19:11
 */
@Data
@Entity
@Table(name = "notifications")
@SQLRestriction("active='1'")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile; // Customer receiving the notification

    private String message;
    private String channel; // SMS, EMAIL, PUSH
    private LocalDateTime dateSend;
    private boolean delivered;
    private String eventType; // LOAN_CREATED, DUE_REMINDER, etc.
    private Integer active=1;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    private int createdBy;
    @UpdateTimestamp
    private LocalDate dateModified;
    private int modifiedBy;
}
