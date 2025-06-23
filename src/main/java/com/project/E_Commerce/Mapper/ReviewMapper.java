package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    //  Create Review
    @Insert("""
        INSERT INTO review (rating, comment, created_at, user_id, product_id)
        VALUES (#{rating}, #{comment}, #{createdAt}, #{userId}, #{productId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "reviewId")
    int createReview(Review review);

    // Update Review
    @Update("""
        UPDATE review
        SET rating = #{rating}, comment = #{comment}, created_at = #{createdAt}
        WHERE review_id = #{reviewId}
    """)
    int updateReview(Review review);

    //  Delete Review by ID
    @Delete("DELETE FROM review WHERE review_id = #{reviewId}")
    int deleteReview(@Param("reviewId") int reviewId);

    // Get Review by ID
    @Select("SELECT * FROM review WHERE review_id = #{reviewId}")
    Review getReviewById(@Param("reviewId") int reviewId);

    // Get All Reviews
    @Select("SELECT * FROM review")
    List<Review> getAllReviews();

    //  Get Reviews by Product ID
    @Select("SELECT * FROM review WHERE product_id = #{productId}")
    List<Review> getReviewsByProductId(@Param("productId") int productId);

    //  Get Reviews by User ID
    @Select("SELECT * FROM review WHERE user_id = #{userId}")
    List<Review> getReviewsByUserId(@Param("userId") int userId);
}
