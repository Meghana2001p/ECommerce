package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User must not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Email type must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    private EmailType emailType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotBlank(message = "Recipient email must not be blank")
    @Email(message = "Invalid email format")
    @Column(name = "recipient_email", nullable = false, length = 255)
    private String recipientEmail;

    @PastOrPresent(message = "Sent time cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @NotNull(message = "Email status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public enum EmailStatus {
        SENT, FAILED, QUEUED, RETRYING
    }

    public enum EmailType {
        ORDER_CONFIRMATION,
        PASSWORD_RESET,
        SHIPPING_UPDATE,
        PROMOTION,
        REFUND
    }
}
