package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.RelatedProduct;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelatedProductRequest {
    @NotNull(message = "Product is required")
    private Integer productId;

    @NotNull(message = "Related product is required")
    private Integer relatedProductId;

    @NotNull(message = "Relationship type is required")
    private RelatedProduct.RelationshipType relationshipType;

}
