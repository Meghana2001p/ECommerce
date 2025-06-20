package com.project.E_Commerce.Entity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Coupon {

    private Integer id;

    @NotBlank(message = "Coupon code must not be blank")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    private String code;

    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount cannot exceed 100%")
    private BigDecimal discountPercent;

    @Future(message = "Expiry date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;
}
