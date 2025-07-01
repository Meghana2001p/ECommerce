package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Payment is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @NotNull(message = "Refund amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Refund amount must be positive")
    @Column(nullable = false, precision = 60, scale = 30)
    private BigDecimal amount;

    @NotNull(message = "Refund status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    @PastOrPresent(message = "Refund date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "refunded_at", nullable = false)
    private LocalDateTime refundedAt = LocalDateTime.now();

    public enum RefundStatus {
        INITIATED,
        PROCESSING,
        SUCCESS,
        FAILED
    }
}
