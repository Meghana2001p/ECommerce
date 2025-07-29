package com.project.E_Commerce.dto.Product;

import lombok.Data;

import java.util.List;

@Data
public class BrandRequest {

    private String brandName;
    private List<Integer> categoryIds;

}
