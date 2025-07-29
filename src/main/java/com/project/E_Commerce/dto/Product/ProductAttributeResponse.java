package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductAttributeResponse {
    private String attributeName;
    private String value;

    public ProductAttributeResponse() {

    }
}
