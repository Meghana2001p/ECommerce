package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAmountSummaryDto {
    private BigDecimal subtotal;         // sum of all item totals
    private BigDecimal discount;         // from coupon
    private BigDecimal tax;              // optional
    private BigDecimal shipping;         // optional
    private BigDecimal totalAmount;      // final amount to pay
    private String couponCode;
}
