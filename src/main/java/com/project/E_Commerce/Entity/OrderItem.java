package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Integer id;

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    // Dynamically calculate total price
    public BigDecimal getTotalPrice() {
        return (price != null && quantity != null)
                ? price.multiply(BigDecimal.valueOf(quantity))
                : BigDecimal.ZERO;
    }
}
