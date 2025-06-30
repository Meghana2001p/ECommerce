package com.project.E_Commerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusAgentUpdateDto {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Delivery status is required")
    private DeliveryState status;

    private String trackingNumber;


    @FutureOrPresent(message = "Estimated delivery date must be today or in the future")
    private LocalDateTime estimatedDeliveryDate;



    public enum DeliveryState {
        PENDING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    }



}
