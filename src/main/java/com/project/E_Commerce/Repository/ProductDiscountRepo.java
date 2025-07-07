package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.ProductDiscount;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
