package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends JpaRepository<Brand,Integer> {
    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE b.brandName = :brandName")
    boolean existsByBrandName(@Param("brandName") String brandName);




}
