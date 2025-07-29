package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Product.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDiscountRepo extends JpaRepository<ProductDiscount,Integer> {
    @Query("""
        SELECT pd FROM ProductDiscount pd
        JOIN FETCH pd.discount d
        WHERE pd.product.id = :productId
        AND d.isActive = true
        AND d.startDate <= CURRENT_TIMESTAMP
        AND d.endDate >= CURRENT_TIMESTAMP
    """)
    List<ProductDiscount> findActiveDiscounts(@Param("productId") Integer productId);

    @Query("SELECT d.discountPercent " +
            "FROM ProductDiscount pd " +
            "JOIN pd.discount d " +
            "WHERE pd.product.id = :productId AND d.isActive = true")
    Optional<BigDecimal> findDiscountPercentByProductId(@Param("productId") Integer productId);




  @Query("delete from ProductDiscount p where p.product.id = :productId")
    void deleteByProductId(Integer productId);


    ProductDiscount findByProductId(Integer productId);






    @Query(value = "SELECT d.* FROM discount d " +
            "JOIN product_discount pd ON d.id = pd.discount_id " +
            "WHERE pd.product_id = :productId", nativeQuery = true)
    Optional<Discount> findDiscountDetailsByProductId(@Param("productId") Integer productId);

}
