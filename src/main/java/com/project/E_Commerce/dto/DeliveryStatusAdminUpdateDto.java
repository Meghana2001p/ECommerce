package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.DeliveryStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusAdminUpdateDto {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus.DeliveryState status;

    @Size(max = 100, message = "Tracking number can't exceed 100 characters")
    private String trackingNumber;

    @Size(max = 100, message = "Carrier can't exceed 100 characters")
    private String carrier;

    @FutureOrPresent(message = "Estimated delivery date must be today or in the future")
    private LocalDateTime estimatedDeliveryDate;

    @NotNull(message = "Delivery type is required")
    private DeliveryStatus.DeliveryType deliveryType;


}
