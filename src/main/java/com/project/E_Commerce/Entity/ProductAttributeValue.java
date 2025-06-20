package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue {

    private Integer id;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Attribute ID is required")
    private Integer attributeId;

    @NotNull(message = "Attribute ID is required")
    @Size(max = 255, message = "Value must be under 255 characters")
    private String value;



}
