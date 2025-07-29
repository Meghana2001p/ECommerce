package com.project.E_Commerce.Entity.Email;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Email type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailType type;

    @NotBlank(message = "Subject must not be blank")
    @Size(max = 255, message = "Subject must be under 255 characters")
    @Column(nullable = false, length = 255)
    private String subject;

    @NotBlank(message = "Body template must not be blank")
    @Lob
    @Column(name = "body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;

    @Column(name = "is_html", nullable = false)
    private Boolean isHtml = true;

    public enum EmailType {
        ORDER_CONFIRMATION,
        PASSWORD_RESET,
        SHIPPING_UPDATE,
        PROMOTION,
        REFUND
    }
}
