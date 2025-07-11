package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.Review;
import com.project.E_Commerce.Entity.User;
import com.project.E_Commerce.Exception.DataBaseException;
import com.project.E_Commerce.Exception.ProductNotFoundException;
import com.project.E_Commerce.Exception.ReviewNotFoundException;
import com.project.E_Commerce.Exception.UserNotFoundException;
import com.project.E_Commerce.Repository.ProductRepo;
import com.project.E_Commerce.Repository.ReviewRepo;
import com.project.E_Commerce.Repository.UserRepo;
import com.project.E_Commerce.Service.ReviewService;
import com.project.E_Commerce.dto.ReviewRequestDto;
import com.project.E_Commerce.dto.ReviewResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);


    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;


    @Override
    public ReviewResponseDto addReview(ReviewRequestDto dto) {
        if(dto==null)
        {
            throw new IllegalArgumentException("The review request cannot be null");
        }
   try {
       Review review = new Review();

       review.setComment(dto.getComment());
       review.setRating(dto.getRating());
       User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
       review.setUser(user);
       Product product = productRepo.findById(dto.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product could not be found"));
       review.setProduct(product);
       review.setCreatedAt(LocalDateTime.now());
       reviewRepo.save(review);

       ReviewResponseDto reviewResponseDto = new ReviewResponseDto();

       reviewResponseDto.setComment(review.getComment());
       reviewResponseDto.setRating(review.getRating());
       reviewResponseDto.setUserName(review.getUser().getName());
       reviewResponseDto.setProductName(review.getProduct().getName());
       reviewResponseDto.setCreatedAt(review.getCreatedAt());

       return reviewResponseDto;
   } catch (DataAccessException e) {
            throw new DataBaseException("Internal Server Error");
        }
   catch (Exception e) {
            logger.error(" Unexpected error had occured inserting the review ");
            throw e ;
        }
    }

    @Override
    public List<ReviewResponseDto> getReviewsByProductId(Integer productId) {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product Id is invalid");
        }
try {
    Product product = productRepo.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product does not exist"));

    List<Review> reviews = reviewRepo.findByProduct(productId);
    if (reviews == null || reviews.isEmpty()) {
        throw new ReviewNotFoundException("No reviews found for product  with ID: " + productId);
    }
    List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
    for (Review review : reviews) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(review.getReviewId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setUserName(review.getUser().getName());
        dto.setProductName(product.getName());  // Reuse the fetched product
        dto.setCreatedAt(review.getCreatedAt());

        reviewResponseDtos.add(dto);
    }

    return reviewResponseDtos;
}catch (DataAccessException e) {
    throw new DataBaseException("Internal Server Error");
}
catch (Exception e) {
    logger.error(" Unexpected error had occured retrieving  the review  using product id ");
    throw e ;
}
    }


    @Override
    public List<ReviewResponseDto> getReviewsByUserId(Integer userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User Id is invalid");
        }

        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User does not exist"));

            List<Review> reviews = reviewRepo.findByUser(userId);
            if (reviews == null || reviews.isEmpty()) {
                throw new ReviewNotFoundException("No reviews found for user with ID: " + userId);
            }
            List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
            for (Review review : reviews) {
                ReviewResponseDto dto = new ReviewResponseDto();
                dto.setReviewId(review.getReviewId());
                dto.setComment(review.getComment());
                dto.setRating(review.getRating());
                dto.setUserName(user.getName()); // Reuse fetched user
                dto.setProductName(review.getProduct().getName());
                dto.setCreatedAt(review.getCreatedAt());

                reviewResponseDtos.add(dto);
            }

            return reviewResponseDtos;
        } catch (DataAccessException e) {
            throw new DataBaseException("Internal Server Error");
        } catch (Exception e) {
            logger.error("Unexpected error occurred retrieving the reviews using user id", e);
            throw e;
        }
    }


    @Override
    public void deleteReview(Integer reviewId) {
        if (reviewId == null || reviewId <= 0) {
            throw new IllegalArgumentException("Review ID is invalid");
        }

        try {
            Review review = reviewRepo.findById(reviewId)
                    .orElseThrow(() -> new ReviewNotFoundException("Review not found "));

            reviewRepo.delete(review);
            logger.info("Review deleted successfully with ID: {}", reviewId);

        } catch (DataAccessException e) {
            throw new DataBaseException("Failed to delete review due to database error");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting review with ID: {}", reviewId, e);
            throw e;
        }
    }

}
