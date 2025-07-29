package com.project.E_Commerce.dto.Payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateRequestDto {
    private Integer orderId;
   // private Payment.PaymentMethod method;
    private BigDecimal amount;
}
