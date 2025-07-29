package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseCart {
    private String couponName;
    private BigDecimal couponDiscountAmount;

    public CouponResponseCart(String couponName, BigDecimal couponDiscountAmount, Object o) {
    }
}