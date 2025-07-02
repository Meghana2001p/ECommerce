package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponse {
    private Integer productId;
    private String productName;
    private String productImageUrl;
    private Boolean available;
    private LocalDateTime createdAt;
}
