package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeDTO {
    private String attributeName;
    private String value;
}
