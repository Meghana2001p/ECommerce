package com.project.E_Commerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponRequest
{

    @NotBlank(message = "Coupon code must not be blank")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    private String code;


    @DecimalMin(value = "0.0", inclusive = false, message = "Discount amount must be greater than 0")
    private BigDecimal discountAmount;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;

    @NotNull(message = "Usage limit is required")
    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;
}
