package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductAttributeValueResponse {
    private Integer id;
    private String value;

    private Integer productId;
    private String productName;

    private Integer attributeId;
    private String attributeName;

    public ProductAttributeValueResponse() {

    }
}
