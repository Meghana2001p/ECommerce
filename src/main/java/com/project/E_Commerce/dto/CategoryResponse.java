package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {
    private Integer categoryId;
    public  String categoryName;
    public Integer parentID;
}
