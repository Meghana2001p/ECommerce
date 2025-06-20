package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatus
{
    private int id;

    @Min(1)
    private int orderId;

    @NotNull(message = "Delivery status must not be null")
    private DeliveryState status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "Update time cannot be in the future")
    private LocalDateTime updatedAt;

    @Size(max = 100, message = "Tracking number can't exceed 100 characters")
    private String trackingNumber;

    @Size(max = 100, message = "Carrier name can't exceed 100 characters")
    @FutureOrPresent(message = "Estimated delivery date must be today or in the future")
    private String carrier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime estimatedDeliveryDate;

    @NotNull(message = "Delivery type must not be null")
    private DeliveryType deliveryType;


    public enum DeliveryState {
        PENDING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    }


    public enum DeliveryType {
        STANDARD, EXPRESS, SAME_DAY, NEXT_DAY
    }
}
