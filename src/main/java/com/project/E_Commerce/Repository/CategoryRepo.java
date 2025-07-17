package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Category;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.categoryName = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT c FROM Category c WHERE c.categoryName = :name")
    Optional<Category> getByName(@Param("name") String name);

    @Query("SELECT c FROM Category c WHERE c.parent.categoryId = :parentId")
    List<Category> getByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> getRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parent = :parent")
    List<Category> getByParent(@Param("parent") Category parent);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.categoryName = :categoryName")
    boolean existsByCategoryName(@Param("categoryName") String categoryName);

}
