package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// SearchHistoryRequest.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryRequest {
    private Integer userId;
    private Integer productId;  // Optional, if viewing a product
    private String keyword;
}
