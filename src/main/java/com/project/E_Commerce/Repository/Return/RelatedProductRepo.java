package com.project.E_Commerce.Repository.Return;

import com.project.E_Commerce.Entity.Product.RelatedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelatedProductRepo extends JpaRepository<RelatedProduct,Integer>
{

    List<RelatedProduct> findByProductId(Integer productId);

    Optional<RelatedProduct> findByProductIdAndRelatedProductId(Integer productId, Integer relatedProductId);

    int  deleteByProductIdAndRelatedProductId(Integer productId, Integer relatedProductId);

    @Query("SELECT rp FROM RelatedProduct rp JOIN FETCH rp.relatedProduct WHERE rp.product.id = :productId")
    List<RelatedProduct> findActiveRelatedByProductId(@Param("productId") Integer productId);

    Optional<RelatedProduct> findByProductIdAndRelatedProductIdAndRelationshipType(Integer productId,
                                                                                   Integer relatedProductId,
                                                                                   RelatedProduct.RelationshipType relationshipType);
}
