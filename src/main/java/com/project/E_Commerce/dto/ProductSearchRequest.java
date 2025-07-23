package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductSearchRequest {
    private String keyword;
    private List<String> brands;
    private List<String> sizes;
    private List<String> colors;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;
    private int page;
    private int size;
}
