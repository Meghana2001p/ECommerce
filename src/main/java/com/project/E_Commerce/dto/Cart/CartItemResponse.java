package com.project.E_Commerce.dto.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal priceBeforeDiscount;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal priceAfterDiscount;
    private Integer quantity;
    private BigDecimal totalPriceForThisItem;
    private Double productRating;
}
