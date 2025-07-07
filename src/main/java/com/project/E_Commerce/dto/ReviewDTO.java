package com.project.E_Commerce.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
