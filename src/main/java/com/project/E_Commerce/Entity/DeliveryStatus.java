package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private int orderId;

    private DeliveryState status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String trackingNumber;
    private String carrier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime estimatedDeliveryDate;

    private DeliveryType deliveryType;


    public enum DeliveryState {
        PENDING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    }


    public enum DeliveryType {
        STANDARD, EXPRESS, SAME_DAY, NEXT_DAY
    }
}
