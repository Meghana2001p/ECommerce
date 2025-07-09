package com.project.E_Commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

@NoArgsConstructor
public class WishlistResponse {
    private Integer productId;
    private String productName;
    private String productImageUrl;
    private String productDescription;
    private BigDecimal price;
    private Boolean available;
    private String brandName;
    private LocalDateTime createdAt;

    public WishlistResponse(Integer id, String name, String imageAddress, Boolean isAvailable,  LocalDateTime createdAt) {

        this.productId = id;
        this.productName = name;
        this.productImageUrl = imageAddress;
        this.available = isAvailable;
        this.createdAt = createdAt;
    }



    public WishlistResponse(
            Integer productId,
            String productName,
            String productImageUrl,
            String productDescription,
            BigDecimal price,
            Boolean available,
            String brandName,
            LocalDateTime createdAt
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productDescription = productDescription;
        this.price = price;
        this.available = available;
        this.brandName = brandName;
        this.createdAt = createdAt;
    }
}
