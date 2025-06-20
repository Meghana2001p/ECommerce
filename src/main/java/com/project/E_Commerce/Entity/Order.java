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
public class Order {

    private Integer id;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @PastOrPresent(message = "Order date must be in the past or present")
    private LocalDateTime orderDate = LocalDateTime.now();

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        REFUNDED
    }
}
