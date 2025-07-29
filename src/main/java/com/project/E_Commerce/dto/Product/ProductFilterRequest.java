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
    private String keyword; // For search by name or description

    private String sortBy;            // e.g., "price", "name"
    private String sortDirection;     // "asc" or "desc"
    private Integer page = 0;         // default to 0
    private Integer size = 10;        // default page size
}
