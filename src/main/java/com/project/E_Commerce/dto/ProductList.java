package com.project.E_Commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data

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

    public ProductList(Integer id,String name,String brandName,String thumbnailUrl,BigDecimal price ,BigDecimal discountedPrice,Integer discountPercentage,Double averageRating,Integer reviewCount, Boolean inStock
   , String label)
    { this.id=id;
        this.name=name;
        this.brandName=brandName;
        this.thumbnailUrl=thumbnailUrl;
        this.price=price;
        this.discountedPrice = discountedPrice;
        this.averageRating=averageRating;
        this.reviewCount=reviewCount;
        this.inStock = inStock;
        this.label=label;


    }


}
