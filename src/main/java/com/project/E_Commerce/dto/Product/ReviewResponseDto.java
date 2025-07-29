package com.project.E_Commerce.dto.Product;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {
    private Integer reviewId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private String userName;
    private String productName;
}
