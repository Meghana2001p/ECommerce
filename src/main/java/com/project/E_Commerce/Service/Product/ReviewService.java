package com.project.E_Commerce.Service.Product;

import com.project.E_Commerce.dto.Product.ReviewRequestDto;
import com.project.E_Commerce.dto.Product.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequestDto dto);
    List<ReviewResponse> getReviewsByProductId(Integer productId);
  List<ReviewResponse> getReviewsByUserId(Integer userId);
   void deleteReview(Integer reviewId);
}
