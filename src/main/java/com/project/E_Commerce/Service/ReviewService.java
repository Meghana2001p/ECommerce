package com.project.E_Commerce.Service;

import com.project.E_Commerce.dto.ReviewRequestDto;
import com.project.E_Commerce.dto.ReviewResponse;
import org.springframework.stereotype.Service;
import java.util.List;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequestDto dto);
    List<ReviewResponse> getReviewsByProductId(Integer productId);
  List<ReviewResponse> getReviewsByUserId(Integer userId);
   void deleteReview(Integer reviewId);
}
