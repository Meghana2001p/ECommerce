package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImage, Integer> {

    // ✅ Get all images by product ID
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImage> findByProductId(Integer productId);

    // ✅ Delete all images by product ID (if needed)
    @Transactional
    void deleteByProductId(Integer productId);

    // ✅ Get only primary image for a product
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isPrimary = true")
    ProductImage findPrimaryImageByProductId(Integer productId);

    // ✅ Count images for a product
    @Query("SELECT COUNT(pi) FROM ProductImage pi WHERE pi.product.id = :productId")
    long countImagesByProductId(Integer productId);

    // ✅ Check if image URL exists for any product (optional uniqueness enforcement)
    boolean existsByImageUrl(String imageUrl);
}
