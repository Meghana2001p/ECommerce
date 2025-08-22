package com.project.E_Commerce.ServiceImplementation.Product;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.Review;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Mapper.ReviewMapper;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.Product.ReviewRepo;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Service.Product.ReviewService;
import com.project.E_Commerce.dto.Product.ReviewRequestDto;
import com.project.E_Commerce.dto.Product.ReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public   class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);


    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public ReviewResponse addReview(ReviewRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("The review request cannot be null");
        }


        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepo.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product could not be found"));

        Review review = reviewMapper.toEntity(dto);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepo.save(review);

        Review savedReview = reviewRepo.findByIdWithUser(review.getReviewId());
        ReviewResponse reviewResponseDto = new ReviewResponse();
        reviewResponseDto.setComment(savedReview.getComment());
        reviewResponseDto.setRating(savedReview.getRating());
        reviewResponseDto.setUserId(savedReview.getUser().getId());
        Integer id = savedReview.getUser().getId();
        User user11 = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        reviewResponseDto.setUserName(user11.getName());
        reviewResponseDto.setCreatedAt(savedReview.getCreatedAt());

        return reviewResponseDto;

    }


    @Override
    public List<ReviewResponse> getReviewsByProductId(Integer productId) {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product Id is invalid");
        }
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product does not exist"));

        List<Review> reviews = reviewRepo.findByProduct(productId);
        if (reviews == null || reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found for product  with ID: " + productId);
        }
        List<ReviewResponse> reviewResponseDtos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponse dto = new ReviewResponse();

            dto.setComment(review.getComment());
            dto.setUserId(review.getUser().getId());
            dto.setRating(review.getRating());
            dto.setUserName(review.getUser().getName());

            dto.setCreatedAt(review.getCreatedAt());

             reviewResponseDtos.add(dto);
        }

        return reviewResponseDtos;


}
    @Override
    public List<ReviewResponse> getReviewsByUserId(Integer userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User Id is invalid");
        }


            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

            List<Review> reviews = reviewRepo.findByUser(userId);
            if (reviews == null || reviews.isEmpty()) {
                throw new IllegalArgumentException("No reviews found for user with ID: " + userId);
            }
            List<ReviewResponse> reviewResponseDtos = new ArrayList<>();
            for (Review review : reviews) {
                ReviewResponse dto = new ReviewResponse();

                dto.setComment(review.getComment());
                dto.setRating(review.getRating());
                dto.setUserId(review.getUser().getId());
                dto.setUserName(user.getName()); // Reuse fetched user
                dto.setCreatedAt(review.getCreatedAt());

                reviewResponseDtos.add(dto);
            }

            return reviewResponseDtos;

    }


    @Override
    public void deleteReview(Integer reviewId) {
        if (reviewId == null || reviewId <= 0) {
            throw new IllegalArgumentException("Review ID is invalid");
        }


            Review review = reviewRepo.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found "));

            reviewRepo.delete(review);
            logger.info("Review deleted successfully with ID: {}", reviewId);

        }
    }


