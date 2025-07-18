package com.project.E_Commerce.dto;

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
