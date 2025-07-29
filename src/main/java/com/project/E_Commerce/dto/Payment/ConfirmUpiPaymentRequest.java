package com.project.E_Commerce.dto.Payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfirmUpiPaymentRequest {
    private Integer orderId; // user must send this
    private BigDecimal amountPaid; // optional, for cross-checking
}
