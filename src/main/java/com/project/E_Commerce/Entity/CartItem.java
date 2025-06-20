package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {

    private Integer id;
    @NotNull(message ="Card id should not be null")
    private Integer cartId;

    @NotNull(message = "Product Id should not be null")
    private Integer productId;

    @NotNull(message = "The quantity should not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")

    private BigDecimal price;
    public BigDecimal getTotalPrice() {
        return price != null && quantity != null
                ? price.multiply(BigDecimal.valueOf(quantity))
                : BigDecimal.ZERO;
    }

}
