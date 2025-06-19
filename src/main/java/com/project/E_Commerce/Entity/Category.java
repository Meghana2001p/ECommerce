package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//what type of cloth
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private Integer categoryId;
    private String categoryName;
    private  Integer parentId;




}
