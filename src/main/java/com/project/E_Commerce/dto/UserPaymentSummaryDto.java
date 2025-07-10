package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserPaymentSummaryDto {
    private String userName;
    private Integer orderId;
    private String transactionId;
    private Payment.PaymentMethod paymentMethod;
    private LocalDateTime paidAt;

    private Integer productId;
    private String productName;
    private String productImageName;
    private String brandName;
    private BigDecimal price;
    private Integer quantity;

}
