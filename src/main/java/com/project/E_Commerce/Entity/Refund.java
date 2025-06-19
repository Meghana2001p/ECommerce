package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Refund {
    private int id;
    private int paymentId;

    private BigDecimal amount;
    private RefundStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundedAt;

    public enum RefundStatus {
        INITIATED,
        PROCESSING,
        SUCCESS,
        FAILED
    }
}
