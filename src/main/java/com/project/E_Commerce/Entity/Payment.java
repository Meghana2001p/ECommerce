package com.project.E_Commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Order is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;



    @NotNull(message = "Payment status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status ;

    @Size(max = 100, message = "Transaction ID must be under 100 characters")
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @PastOrPresent(message = "Payment date cannot be in the future")
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;



    public enum PaymentStatus {
        SUCCESS,
        PENDING,
        FAILED,
        REFUNDED
    }
}
