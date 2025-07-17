package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductAttributeValueResponse {
    private Integer id;
    private String value;
    private Integer productId;
    private Integer attributeId;
}
