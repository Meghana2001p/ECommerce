package com.project.E_Commerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductAttributeAssignmentRequest {
    @NotNull(message = "Product ID must not be null")
    private Integer productId;

    @NotEmpty(message = "Attributes list must not be empty")
    @Valid
    private List<@Valid AttributeValuePair> attributes;

    @Data
    public static class AttributeValuePair {

        @NotNull(message = "Attribute ID must not be null")
        private Integer attributeId;

        @NotBlank(message = "Attribute value must not be blank")
        private String value;
    }
}
