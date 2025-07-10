package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.Payment;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateRequestDto {
    private Integer orderId;
    private Payment.PaymentMethod method;
    private BigDecimal amount;
}
