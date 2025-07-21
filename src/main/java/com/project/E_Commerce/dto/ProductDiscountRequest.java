package com.project.E_Commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDiscountRequest
{

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Discount ID is required")
    private Integer discountId;
}
