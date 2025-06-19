package com.project.E_Commerce.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private Integer reviewId;
    private Integer rating;
    private String comment;
    private LocalDateTime created_at;
     private Integer userId;
    private Integer productId;


}
