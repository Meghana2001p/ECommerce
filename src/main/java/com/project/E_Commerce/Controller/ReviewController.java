package com.project.E_Commerce.Controller;

import com.project.E_Commerce.dto.ReviewRequestDto;
import com.project.E_Commerce.dto.ReviewResponseDto;
import com.project.E_Commerce.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
  @Autowired
    private  ReviewService reviewService;


    @PostMapping("/add")
    public ResponseEntity<ReviewResponseDto> addReview(@Validated @RequestBody ReviewRequestDto dto) {
        ReviewResponseDto created = reviewService.addReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getByProduct(@PathVariable Integer productId) {
        List<ReviewResponseDto> list = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getByUser(@PathVariable Integer userId) {
        List<ReviewResponseDto> list = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(list);
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}

