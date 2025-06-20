package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

    private Integer id;

    @NotBlank(message = "Discount name is required")
    @Size(max = 100, message = "Discount name must be under 100 characters")
    private String name;

    @NotNull(message = "Discount percent is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Discount must be greater than 0%")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount cannot exceed 100%")
    private BigDecimal discountPercent;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private Boolean isActive = true;
}
