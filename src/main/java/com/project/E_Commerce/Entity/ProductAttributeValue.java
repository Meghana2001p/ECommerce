package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue {

    private Integer id;
    private Integer productId;
    private Integer attributeId;
    private String value;



}
