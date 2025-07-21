package com.project.E_Commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data

@Builder
public class ProductList {
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
    private String label; // Optional, e.g., "New", "Best Seller"
    private Boolean inWishlist;
    private Boolean inCart;

    public ProductList(Integer id,String name,String brandName,String description,String thumbnailUrl,BigDecimal price ,BigDecimal discountedPrice,Integer discountPercentage,Double averageRating,Integer reviewCount, Boolean inStock
   , String label)
    {
        this.id=id;
        this.name=name;
        this.brandName=brandName;
        this.description=description;
        this.thumbnailUrl=thumbnailUrl;
        this.price=price;
        this.discountedPrice = discountedPrice;
        this.averageRating=averageRating;
        this.reviewCount=reviewCount;
        this.inStock = inStock;
        this.label=label;


    }


    public ProductList(Integer id, @NotBlank(message = "Product name is required") @Size(max = 255, message = "Product name must be under 255 characters") String name, @NotNull(message = "Brand name cannot be null") String brandName, @NotBlank(message = "Description is required") String description, @NotBlank(message = "Image address is required") String imageAddress, @NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero") BigDecimal price, BigDecimal discountPrice, int discountPercentage, double averageRating, Integer reviewCount, Boolean isAvailable, String label, boolean inWishlist, boolean inCart) {

        this.id=id;
        this.name=name;
        this.brandName=brandName;
        this.description=description;
        this.thumbnailUrl=thumbnailUrl;
        this.price=price;
        this.discountedPrice = discountedPrice;
        this.averageRating=averageRating;
        this.reviewCount=reviewCount;
        this.inStock = inStock;
        this.label=label;
        this.inWishlist=inWishlist;
        this.inCart=inCart;

    }

    public ProductList() {

    }
}
