package com.project.E_Commerce.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class WishlistResponse {
    private Integer id;
    private String name;
    private String brandName;
    private String description;
    private String thumbnailUrl;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer discountPercentage;
    private Double averageRating;
    private Integer reviewCount;
    private Boolean inStock;
    private String label; // optional
    private List<String> imageUrls;
}
