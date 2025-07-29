package com.project.E_Commerce.dto.Product;

import com.project.E_Commerce.Entity.Product.RelatedProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RelatedProductResponse {
    private Integer productId;
    private String name;
    private String thumbnailUrl;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer discountPercent;
    private Double averageRating;
    private Boolean isAvailable;
    private RelatedProduct.RelationshipType relationshipType;

    public RelatedProductResponse() {

    }
}
