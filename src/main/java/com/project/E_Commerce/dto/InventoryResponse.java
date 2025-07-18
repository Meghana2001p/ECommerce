package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InventoryResponse {
    private Boolean inStock;
    private Integer stockQuantity;
    private LocalDateTime lastUpdated;

    public InventoryResponse() {

    }
}
