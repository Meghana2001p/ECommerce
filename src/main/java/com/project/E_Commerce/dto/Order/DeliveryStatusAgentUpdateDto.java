package com.project.E_Commerce.dto.Order;

import com.project.E_Commerce.Entity.Order.DeliveryStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusAgentUpdateDto {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus.DeliveryState status;

    private String trackingNumber;


    @FutureOrPresent(message = "Estimated delivery date must be today or in the future")
    private LocalDateTime estimatedDeliveryDate;







}
