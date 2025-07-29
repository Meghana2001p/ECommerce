package com.project.E_Commerce.dto.User;

import lombok.Data;

import java.time.LocalDateTime;

// SearchHistoryResponse.java
@Data
public class SearchHistoryResponse {
    private Integer searchId;
    private String keyword;
    private LocalDateTime searchedAt;
    private Integer productId;
    private String productName;
    private String productImageUrl;
}

