package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RelatedProductResponse {
    private Integer productId;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private String relationshipType;

    public RelatedProductResponse() {

    }
}
