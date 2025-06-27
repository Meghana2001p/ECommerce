package com.project.E_Commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private int cartId;
    private int productId;
    private String productName;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String sku;
    private Boolean isAvailable;
    private String brandName;
    private int quantity;
    private BigDecimal totalPrice;    // price × quantity

}

