package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Integer userId;
    private List<CartItemResponse> cartItems;
    private CartSummaryResponse summary;
    private CouponResponseCart coupon; // null if no coupon is applied
}
