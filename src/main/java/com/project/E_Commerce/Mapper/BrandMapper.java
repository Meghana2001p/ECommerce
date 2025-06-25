package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;
//Admin and maybe the sellers too
@Mapper
public interface BrandMapper {

    // Create Brand
    @Insert("INSERT INTO brand (brand_name, category_id) " +
            "VALUES (#{brandName}, #{categoryId})")
    @Options(useGeneratedKeys = true, keyProperty = "brandId")
    int createBrand(Brand brand);

    // Update Brand
    @Update("UPDATE brand SET brand_name = #{brandName}, category_id = #{categoryId} " +
            "WHERE brand_id = #{brandId}")
    int updateBrand(Brand brand);

    // Delete Brand
    @Delete("DELETE FROM brand WHERE brand_id = #{brandId}")
    int deleteBrand(@Param("brandId") int brandId);

    // Get Brand by ID
    @Select("SELECT * FROM brand WHERE brand_id = #{brandId}")
    Brand getBrandById(@Param("brandId") int brandId);

    @Select("SELECT * FROM brand WHERE brand_name = #{brandName}")
    boolean getBrandByBrandName(@Param("brandName") String  brandName);

    @Select("SELECT * FROM brand WHERE brand_id = #{brandId}")
    Brand getBrandBy(@Param("brandId") int brandId);

    // Get All Brands
    @Select("SELECT * FROM brand")
    List<Brand> getAllBrands();

    // Get Brands by Category
    @Select("SELECT * FROM brand WHERE category_id = #{categoryId}")
    List<Brand> getBrandsByCategory(@Param("categoryId") int categoryId);
}
