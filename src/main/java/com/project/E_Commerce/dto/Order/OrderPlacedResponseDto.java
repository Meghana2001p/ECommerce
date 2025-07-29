package com.project.E_Commerce.dto.Order;
import com.project.E_Commerce.Entity.Order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedResponseDto {
    private String message;
    private Integer orderId;
    private String trackingNumber;
    private LocalDateTime estimatedDeliveryDate;
    private Order.OrderStatus orderStatus;
    private BigDecimal totalAmount;
}
