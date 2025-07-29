package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.dto.Product.ProductWithBrandProjection;
import com.project.E_Commerce.dto.User.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // search
    @Query(value = """
                SELECT DISTINCT 
                    p.product_id AS productId,
                    p.name AS name,
                    p.price AS price,
                    b.brand_name AS brandName,
                    (
                        SELECT pi.image_url
                        FROM product_image pi
                        WHERE pi.product_id = p.product_id AND pi.is_primary = 1
                        LIMIT 1
                    ) AS imageUrl
            
                FROM product p
                    LEFT JOIN brand b ON p.brand_id = b.brand_id
                    LEFT JOIN product_attribute_value pav_color
                        ON p.product_id = pav_color.product_id
                    LEFT JOIN product_attribute pa_color
                        ON pav_color.attribute_id = pa_color.attribute_id
                        AND pa_color.name = 'Color'
            
                    LEFT JOIN product_attribute_value pav_size
                        ON p.product_id = pav_size.product_id
                    LEFT JOIN product_attribute pa_size
                        ON pav_size.attribute_id = pa_size.attribute_id
                        AND pa_size.name = 'Size'
            
                    LEFT JOIN brand_category bc ON b.brand_id = bc.brand_id
                    LEFT JOIN category c ON bc.category_id = c.category_id
            
                WHERE p.is_available = 1
            
                    AND (
                        :keyword IS NULL 
                        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(b.brand_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(c.category_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR (pa_color.name = 'Color' AND LOWER(pav_color.value) LIKE LOWER(CONCAT('%', :keyword, '%')))
                        OR (pa_size.name = 'Size' AND LOWER(pav_size.value) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    )
            
                    AND (:brand IS NULL OR LOWER(b.brand_name) = LOWER(:brand))
                    AND (:color IS NULL OR LOWER(pav_color.value) = LOWER(:color))
                    AND (:sizesLength = 0 OR (pa_size.name = 'Size' AND pav_size.value IN (:sizes)))
                    AND (:categoriesLength = 0 OR LOWER(c.category_name) IN (:categories))
                    AND (:minPrice IS NULL OR p.price >= :minPrice)
                    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            
                    AND (
                        :rating IS NULL OR EXISTS (
                            SELECT 1
                            FROM review r
                            WHERE r.product_id = p.product_id
                              AND r.rating = :rating
                        )
                    )
            
                LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<SearchResponse> searchProducts(
            @Param("keyword") String keyword,
            @Param("brand") String brand,
            @Param("color") String color,
            @Param("sizes") List<String> sizes,
            @Param("sizesLength") int sizesLength,
            @Param("categories") List<String> categories,
            @Param("categoriesLength") int categoriesLength,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("rating") Integer rating,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


@Query(value = "select * from  product where product_id =:productID", nativeQuery = true)
    Product isAvaliable( @Param("productID") Integer productId);
}
