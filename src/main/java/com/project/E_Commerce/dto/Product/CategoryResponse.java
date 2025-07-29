package com.project.E_Commerce.dto.Product;

import lombok.Data;

@Data
public class CategoryResponse {
    private Integer categoryId;
    public  String categoryName;
    public Integer parentID;
}
