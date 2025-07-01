package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Order ID must not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Delivery status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryState status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "Update time cannot be in the future")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Size(max = 100, message = "Tracking number can't exceed 100 characters")
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Size(max = 100, message = "Carrier name can't exceed 100 characters")
    @Column(length = 100)
    private String carrier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Estimated delivery date must be today or in the future")
    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @NotNull(message = "Delivery type must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType;

    public enum DeliveryState {
        PENDING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    }

    public enum DeliveryType {
        STANDARD, EXPRESS, SAME_DAY, NEXT_DAY
    }
}
