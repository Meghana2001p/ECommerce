package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponseDto {
    private String code;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private LocalDateTime expiryDate;
}
