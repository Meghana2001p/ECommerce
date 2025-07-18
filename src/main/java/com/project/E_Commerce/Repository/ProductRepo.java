package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.dto.ProductWithBrandProjection;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query("""
        SELECT p FROM Product p
        JOIN FETCH p.brand b
        WHERE p.isAvailable = true
    """)
    List<Product> findAllAvailableProducts();

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.brand
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithBrand(@Param("id") Integer id);

    @Query("select p from Product p")
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);


    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.sku = :sku")
    boolean existsBySku(@Param("sku") String sku);


    @Query(value = """
    SELECT 
        p.product_id AS productId,
        p.name AS name,
        p.sku AS sku,
        p.price AS price,
        p.image_address AS imageAddress,
        p.description AS description,
        p.is_available AS isAvailable,
        p.brand_id AS brandId,
        b.brand_name AS brandName
    FROM product p
    JOIN brand b ON p.brand_id = b.brand_id
    WHERE p.product_id = :productId
    """, nativeQuery = true)
    Optional<ProductWithBrandProjection> findWithBrand(@Param("productId") Integer productId);




}
