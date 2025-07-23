package com.project.E_Commerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SearchRequest {
    private String keyword;
    private  String categories;
    private String brand;
    private Double minPrice;
    private Double maxPrice;
    private Integer page;
    private List<String> sizes;
    private String sortBy;
    private Double ratings;
    private String color;
}
