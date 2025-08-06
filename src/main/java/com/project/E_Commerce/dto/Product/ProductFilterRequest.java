package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {
    private List<String> colors;
    private List<String> sizes;
    private Float minPrice;
    private Float maxPrice;
    private List<String> brands;
    private List<String> categories;
    private String keyword;

    private String sortBy;
    private String sortDirection;
    private Integer page = 0;
    private Integer size = 10;
}
