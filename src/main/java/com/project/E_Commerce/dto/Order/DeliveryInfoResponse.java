package com.project.E_Commerce.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DeliveryInfoResponse {
    private String carrier;
    private String deliveryType;
    private String status;
    private LocalDateTime estimatedDeliveryDate;
    private String trackingNumber;
}
