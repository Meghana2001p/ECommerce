package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ReviewResponse {

    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
    private Integer userId;
    private String userName;

    public ReviewResponse() {

    }
}