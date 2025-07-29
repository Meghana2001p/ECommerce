package com.project.E_Commerce.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserPaymentSummaryDto {
    private String userName;
    private Integer orderId;
    private String transactionId;
    //private Payment.PaymentMethod paymentMethod;
    private LocalDateTime paidAt;

    private Integer productId;
    private String productName;
    private String productImageName;
    private String brandName;
    private BigDecimal price;
    private Integer quantity;


    public UserPaymentSummaryDto(String userName, Integer orderId, String transactionId,
                                 LocalDateTime paidAt, Integer productId, String productName,
                                 String productImageName, String brandName, BigDecimal price,
                                 Integer quantity) {
        this.userName = userName;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.paidAt = paidAt;
        this.productId = productId;
        this.productName = productName;
        this.productImageName = productImageName;
        this.brandName = brandName;
        this.price = price;
        this.quantity = quantity;
    }
}