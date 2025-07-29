package com.project.E_Commerce.Entity.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.E_Commerce.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "shipping_address", nullable = false, length = 255)
    private String shippingAddress;


    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "is_gift", nullable = false)
    private boolean isGift = false;

    @PastOrPresent(message = "Order date must be in the past or present")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "Order status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    @JsonIgnore
    @Column(name = "total_amount", precision = 30, scale = 20, nullable = false)
    private BigDecimal totalAmount;


    public enum OrderStatus {
        CONFIRMED,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        OUT_FOR_DELIVERY;

    }

    public enum PaymentMethod {
        COD,
        UPI,
        CREDIT_CARD,
        DEBIT_CARD,
        NET_BANKING,
        WALLET
    }
}
