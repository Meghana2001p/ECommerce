package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review,Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Integer countByProductId(@Param("productId") Integer productId);


    @Query("select r from Review r where r.product.id=:productId")
    List<Review> findByProduct(@Param("productId")Integer productId);

//    @Query("select r from Review r where r.product.id=productId")
//    List<Review> findByProduct(Integer productId);

     @Query("select r from Review r where r.user.id=:userId")
    List<Review> findByUser(Integer userId);


    @Query(
            value = "SELECT * FROM review WHERE product_id = :productId",
            nativeQuery = true
    )
    List<Review> findByProductId(@Param("productId") Integer productId);
    @Query(
            value = "SELECT AVG(rating) FROM review WHERE product_id = :productId",
            nativeQuery = true
    )
    Double findAverageRating(@Param("productId") Integer productId);

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.id = :id")
    Review findByIdWithUser(@Param("id") Integer id);


}
