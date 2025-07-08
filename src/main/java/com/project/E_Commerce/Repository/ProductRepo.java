package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}
