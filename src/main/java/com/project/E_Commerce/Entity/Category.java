package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

//what type of cloth
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private Integer categoryId;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be under 100 characters")
    private String categoryName;

    private  Integer parentId;




}
