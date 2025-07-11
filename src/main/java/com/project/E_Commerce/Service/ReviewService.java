package com.project.E_Commerce.Service;

import com.project.E_Commerce.dto.ReviewRequestDto;
import com.project.E_Commerce.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto addReview(ReviewRequestDto dto);
    List<ReviewResponseDto> getReviewsByProductId(Integer productId);
    List<ReviewResponseDto> getReviewsByUserId(Integer userId);
    void deleteReview(Integer reviewId);
}
