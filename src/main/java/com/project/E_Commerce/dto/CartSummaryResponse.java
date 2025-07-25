package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponse {
    private BigDecimal subTotal;         // total after item-level discounts
    private BigDecimal totalGST;         // e.g., 18% GST
    private BigDecimal couponDiscount;   // total discount from coupon
    private BigDecimal grandTotal;       // (subTotal + GST) - couponDiscount
    private Integer totalItems;          // unique products
    private Integer totalQuantity;       // sum of quantities
}