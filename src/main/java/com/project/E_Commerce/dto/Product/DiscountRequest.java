package com.project.E_Commerce.dto.Product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiscountRequest {
    @NotBlank(message = "Discount name is required")
    @Size(max = 100, message = "Discount name must be under 100 characters")
    @Column(length = 100, nullable = false)
    private String name;

    @NotNull(message = "Discount percent is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Discount must be greater than 0%")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount cannot exceed 100%")
    private BigDecimal discountPercent;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;


    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
}
