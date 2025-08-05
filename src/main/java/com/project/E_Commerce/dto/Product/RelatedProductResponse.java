package com.project.E_Commerce.dto.Product;

import com.project.E_Commerce.Entity.Product.RelatedProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RelatedProductResponse {
    private Integer productId;
    private String imageAdress;
    private String name;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPercent;
    private BigDecimal discountPrice;
    private Double averageRating;
    private Boolean isAvailable;
    private RelatedProduct.RelationshipType relationshipType;

    public RelatedProductResponse() {

    }
}
