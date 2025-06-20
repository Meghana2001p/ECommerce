package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Size(max = 100, message = "Transaction ID must be under 100 characters")
    private String transactionId;

    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDateTime paidAt = LocalDateTime.now();

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        UPI,
        NET_BANKING,
        WALLET,
        COD
    }

    public enum PaymentStatus {
        SUCCESS,
        FAILED,
        PENDING,
        CANCELLED,
        REFUNDED
    }
}
