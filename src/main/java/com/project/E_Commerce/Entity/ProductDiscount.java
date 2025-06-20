package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDiscount {

    private Integer id;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Discount ID is required")
    private Integer discountId;
}
