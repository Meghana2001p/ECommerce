package com.project.E_Commerce.Entity.Email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.E_Commerce.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_queue")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Notification type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @NotBlank(message = "Message must not be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @NotNull(message = "Notification status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @NotNull(message = "Scheduled time is required")
    @FutureOrPresent(message = "Scheduled time must be now or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    public enum NotificationType {
        EMAIL, SMS, IN_APP
    }

    public enum NotificationStatus {
        PENDING, SENT, FAILED, CANCELLED
    }
}
