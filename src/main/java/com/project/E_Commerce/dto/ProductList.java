package com.project.E_Commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductList {
    private Integer id;
    private String name;
    private String brandName;
    private String thumbnailUrl;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer discountPercentage;
    private Double averageRating;
    private Integer reviewCount;
    private Boolean inStock;
    private String label; // Optional, e.g., "New", "Best Seller"
    private Boolean inWishlist;
    private Boolean inCart;

    public ProductList(Integer id, @NotBlank(message = "Product name is required") @Size(max = 255, message = "Product name must be under 255 characters") String name, @NotNull(message = "Brand name cannot be null") String brandName, @NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero") BigDecimal price, BigDecimal discountPrice, int i, double v, Integer reviewCount, Boolean isAvailable, Object o, boolean inWishlist, boolean inCart) {
    }
}
