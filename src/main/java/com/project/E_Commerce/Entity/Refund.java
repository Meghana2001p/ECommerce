package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Refund {
    private Integer id;
    @Min(value = 1, message = "Payment ID must be greater than 0")
    private Integer paymentId;
    @NotNull(message = "Refund amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Refund amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Refund status cannot be null")
    private RefundStatus status;
    @PastOrPresent(message = "Refund date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundedAt=LocalDateTime.now();

    public enum RefundStatus {
        INITIATED,
        PROCESSING,
        SUCCESS,
        FAILED
    }
}
