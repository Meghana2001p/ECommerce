package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Review;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review,Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Integer countByProductId(@Param("productId") Integer productId);

    List<Review> findByProductId(Integer productId);
}
