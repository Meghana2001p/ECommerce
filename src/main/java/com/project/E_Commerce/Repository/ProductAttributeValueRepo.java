package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.ProductAttribute;
import com.project.E_Commerce.Entity.ProductAttributeValue;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductAttributeValueRepo extends JpaRepository<ProductAttributeValue,Integer> {
    // ✅ Get all values for a specific product
    @Query("SELECT pav FROM ProductAttributeValue pav WHERE pav.product.id = :productId")
    List<ProductAttributeValue> findAllByProductId(Integer productId);

    // ✅ Get all values for a specific attribute
    @Query("SELECT pav FROM ProductAttributeValue pav WHERE pav.attribute.id = :attributeId")
    List<ProductAttributeValue> findAllByAttributeId(Integer attributeId);

    // ✅ Get values for a product and attribute combination
    @Query("SELECT pav FROM ProductAttributeValue pav WHERE pav.product.id = :productId AND pav.attribute.id = :attributeId")
    List<ProductAttributeValue> findByProductIdAndAttributeId(Integer productId, Integer attributeId);

    // ✅ Check if value exists for a product & attribute
    @Query("SELECT COUNT(pav) > 0 FROM ProductAttributeValue pav WHERE pav.product.id = :productId AND pav.attribute.id = :attributeId AND pav.value = :value")
    boolean existsByProductIdAndAttributeIdAndValue(Integer productId, Integer attributeId, String value);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductAttributeValue pav WHERE pav.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Integer productId);

    @Query("SELECT pav FROM ProductAttributeValue pav WHERE pav.product.id = :productId")
    List<ProductAttributeValue> findByProductId(Integer productId);
}
