package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImage, Integer> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId")
    List<ProductImage> findByProductId(Integer productId);

    @Query(
            value = "SELECT * FROM product_image WHERE product_id = :productId ORDER BY is_primary DESC",
            nativeQuery = true
    )
    List<ProductImage> findByProductIdOrderByIsPrimary(@Param("productId") Integer productId);

    int deleteByProductId(Integer productId);


}
