package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CouponResponse1 {
    private String code;
    private BigDecimal discountAmount;
    private LocalDateTime expiryDate;

    public CouponResponse1() {

    }
}
