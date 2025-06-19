package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Integer id;
    private Integer orderId;
    private PaymentMethod paymentMethod; // ✅ Enum instead of raw String
    private PaymentStatus status;
    private String transactionId;

    private LocalDateTime paidAt;

    private BigDecimal amount;

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, WALLET, COD
    }


    public enum PaymentStatus {
        SUCCESS, FAILED, PENDING, CANCELLED, REFUNDED
    }
}
