package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory
{
    private Integer id;
    @NotNull(message = "Product ID is required")
    private Integer productId;
    @NotNull(message = "Old price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Old price must be positive")
    private BigDecimal oldPrice;
    @NotNull(message = "Old price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Old price must be positive")
    private BigDecimal newPrice;

    @PastOrPresent(message = "Change date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changedAt=LocalDateTime.now();
}
