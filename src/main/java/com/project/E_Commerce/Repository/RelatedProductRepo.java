package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.RelatedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelatedProductRepo extends JpaRepository<RelatedProduct,Integer>
{

    // Get all related products for a given main product
    List<RelatedProduct> findByProductId(Integer productId);

    // Check if a specific relationship exists
    Optional<RelatedProduct> findByProductIdAndRelatedProductId(Integer productId, Integer relatedProductId);

    // Delete a specific relation
    int  deleteByProductIdAndRelatedProductId(Integer productId, Integer relatedProductId);

    List<RelatedProduct> findActiveRelatedByProductId(Integer productId);

    Optional<RelatedProduct> findByProductIdAndRelatedProductIdAndRelationshipType(Integer productId,
                                                                                   Integer relatedProductId,
                                                                                   RelatedProduct.RelationshipType relationshipType);
}
