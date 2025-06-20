package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailLog {

    private Integer id;

    @NotNull(message = "User id should not be empty")
    private Integer userId;

    @NotNull(message = "Email type should not be empty")
    private EmailType emailType;

    private Integer orderId;

    @NotBlank
    @Email
    private String recipientEmail;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentAt;

    @NotNull(message = "Email status should not be empty")
    private EmailStatus status;

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
