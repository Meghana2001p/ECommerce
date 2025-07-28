package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.Payment;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentRequestDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
   // private Payment.PaymentMethod method;
    private Payment.PaymentStatus status;
    private String transactionId;
    private LocalDateTime paidAt;
    private BigDecimal amount;
}
