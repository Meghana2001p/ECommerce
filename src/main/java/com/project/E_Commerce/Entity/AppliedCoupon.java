package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppliedCoupon {

    private Integer id;

    @NotNull(message = "Order ID cannot be null")
    @Min(value = 1, message = "Order ID must be greater than 0")
    private Integer orderId;

    @NotNull(message = "Coupon ID cannot be null")
    @Min(value = 1, message = "Coupon ID must be greater than 0")
    private Integer couponId;
}