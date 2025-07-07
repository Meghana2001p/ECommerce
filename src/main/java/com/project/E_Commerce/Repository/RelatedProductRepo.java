package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.RelatedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatedProductRepo extends JpaRepository<RelatedProduct,Integer> {
    @Query("Select rp from RelatedProduct rp where rp.productId=:productId")
     List<RelatedProduct> findByProductIdAndIsActiveTrue(Integer productId);

}
