package com.project.E_Commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok .AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "user_email_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "email_type"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Email type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    private EmailType emailType;

    @Column(name = "is_subscribed", nullable = false)
    private Boolean isSubscribed = true;

    public enum EmailType {
        ORDER_UPDATES,
        PROMOTIONS,
        PASSWORD_NOTIFICATIONS
    }
}
