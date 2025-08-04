package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer>
{
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.categoryName = :categoryName")
    boolean existsByCategoryName(@Param("categoryName") String categoryName);

}
