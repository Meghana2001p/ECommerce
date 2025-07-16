package com.project.E_Commerce.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ProductAttributeAssignmentRequest {
    private Integer productId;

    @NotEmpty(message = "Attributes list must not be empty")
    private List<AttributeValuePair> attributes;

    @Data
    public static class AttributeValuePair {
        private Integer attributeId;
        private String value;
    }
}
