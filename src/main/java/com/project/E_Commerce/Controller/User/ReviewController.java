package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Service.Product.ReviewService;
import com.project.E_Commerce.dto.Product.ReviewRequestDto;
import com.project.E_Commerce.dto.Product.ReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController
{
  @Autowired
    private ReviewService reviewService;


    @PostMapping("/")
    public ResponseEntity<?> addReview(@Validated @RequestBody ReviewRequestDto dto)
    {
        ReviewResponse created = reviewService.addReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<?>> getByProduct(@PathVariable Integer productId) {
        List<ReviewResponse> list = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<?>> getByUser(@PathVariable Integer userId) {
        List<ReviewResponse> list = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(list);
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("deleted successfully");
    }
}

