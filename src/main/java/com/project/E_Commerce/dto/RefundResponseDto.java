package com.project.E_Commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RefundResponseDto {
    private Integer refundId;
    private Integer orderId;
    private Integer paymentId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime refundedAt;
    private String message;

    public RefundResponseDto(Integer id, Integer id1, @NotNull(message = "Refund amount cannot be null") @DecimalMin(value = "0.01", inclusive = true, message = "Refund amount must be positive") BigDecimal amount, String name, @PastOrPresent(message = "Refund date cannot be in the future") LocalDateTime refundedAt, String s) {
        this.refundId = refundId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.refundedAt = refundedAt;
        this.message = message;
    }



    public RefundResponseDto(
            Integer refundId,
            Integer orderId,
            Integer paymentId,
            BigDecimal amount,
            String status,
            LocalDateTime refundedAt,
            String message
    ) {
        this.refundId = refundId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.refundedAt = refundedAt;
        this.message = message;
    }
}

