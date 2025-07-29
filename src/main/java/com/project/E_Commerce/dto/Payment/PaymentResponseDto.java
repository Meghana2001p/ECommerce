package com.project.E_Commerce.dto.Payment;

import com.project.E_Commerce.Entity.Payment.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private Integer id;
    private Integer orderId;
    //private Payment.PaymentMethod method;
    private Payment.PaymentStatus status;
    private String transactionId;
    private LocalDateTime paidAt;
    private BigDecimal amount;
}
