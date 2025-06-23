package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;
// the Admin only he can update and the maybe seller too for sometime I guess
@Mapper
public interface CategoryMapper {

    // Create Category
    @Insert("INSERT INTO category (category_name, parent_id) " +
            "VALUES (#{categoryName}, #{parentId})")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    int createCategory(Category category);

    // Update Category
    @Update("UPDATE category SET category_name = #{categoryName}, parent_id = #{parentId} " +
            "WHERE category_id = #{categoryId}")
    int updateCategory(Category category);

    // Delete Category
    @Delete("DELETE FROM category WHERE category_id = #{categoryId}")
    int deleteCategory(@Param("categoryId") int categoryId);

    // Get Category by ID
    @Select("SELECT * FROM category WHERE category_id = #{categoryId}")
    Category getCategoryById(@Param("categoryId") int categoryId);

    // Get All Categories
    @Select("SELECT * FROM category")
    List<Category> getAllCategories();

    // Get Subcategories by Parent ID
    @Select("SELECT * FROM category WHERE parent_id = #{parentId}")
    List<Category> getSubCategories(@Param("parentId") int parentId);
}
