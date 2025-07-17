package com.project.E_Commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private Integer id;
    private String name;
    private String description;
    private String sku;
    private String brandName;

    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer discountPercentage;
    private Boolean inStock;

    private List<String> imageUrls;
    private List<ProductAttributeDTO> attributes;
    private List<String> availableSizes;
    private List<String> availableColors;

    private List<String> activeOffers;
    private String returnPolicy;
    private String deliveryInfo;

    private List<RelatedProductDTO> relatedProducts;

    private Double averageRating;
    private Integer reviewCount;
    private List<ReviewDTO> reviews;

    private Boolean inWishlist;
    private Boolean inCart;


}
