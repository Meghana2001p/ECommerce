package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Brand;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface BrandRepo extends JpaRepository<Brand,Integer> {
    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE b.brandName = :brandName")
    boolean existsByBrandName(@Param("brandName") String brandName);




}
