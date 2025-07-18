package com.project.E_Commerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

// ReviewRequestDto.java
@Data
public class ReviewRequestDto {
    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    private String comment;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer productId;

}
