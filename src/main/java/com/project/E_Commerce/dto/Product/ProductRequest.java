package com.project.E_Commerce.dto.Product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String imageAddress;
    private BigDecimal price;
    private String sku;
    private Boolean isAvailable;
    private Integer brandId;
    private List<String> imageUrls;
}
