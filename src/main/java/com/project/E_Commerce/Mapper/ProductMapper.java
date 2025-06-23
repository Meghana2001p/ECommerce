package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    //1.createProduct
    //2.updateProduct
    //3.delete product based on the name
    //4.delete product based on the id
    //5. get all the products
    //6.get the products by brandname category name and any name either the category or the normal name

    //Create Product
    @Insert("""
        INSERT INTO product (name, description, image_address, price, sku, is_available, brand_id)
        VALUES (#{name}, #{description}, #{imageAddress}, #{price}, #{sku}, #{isAvailable}, #{brandId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int createProduct(Product product);

    //Update Product
    @Update("""
        UPDATE product
        SET name = #{name}, description = #{description}, image_address = #{imageAddress},
            price = #{price}, sku = #{sku}, is_available = #{isAvailable}, brand_id = #{brandId}
        WHERE product_id = #{productId}
    """)
    int updateProduct(Product product);

    //Delete by ID
    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    int deleteProductById(@Param("productId") int productId);

    // Delete by Name
    @Delete("DELETE FROM product WHERE name = #{name}")
    int deleteProductByName(@Param("name") String name);

    // Get All Products
    @Select("SELECT * FROM product")
    List<Product> getAllProducts();

    //Get Product by ID
    @Select("SELECT * FROM product WHERE product_id = #{productId}")
    Product getProductById(@Param("productId") int productId);

    //Get Products by Brand ID
    @Select("SELECT * FROM product WHERE brand_id = #{brandId}")
    List<Product> getProductsByBrandId(@Param("brandId") int brandId);

    //Get Products by Category Name
    @Select("""
        SELECT p.*
        FROM product p
        JOIN brand b ON p.brand_id = b.brand_id
        JOIN category c ON b.category_id = c.category_id
        WHERE LOWER(c.category_name) LIKE CONCAT('%', LOWER(#{categoryName}), '%')
    """)
    List<Product> getProductsByCategoryName(@Param("categoryName") String categoryName);

    //Get Products by Brand Name
    @Select("""
        SELECT p.*
        FROM product p
        JOIN brand b ON p.brand_id = b.brand_id
        WHERE LOWER(b.brand_name) LIKE CONCAT('%', LOWER(#{brandName}), '%')
    """)
    List<Product> getProductsByBrandName(@Param("brandName") String brandName);

    //Unified Search by Product Name, Brand Name, or Category Name
    @Select("""
        SELECT p.*
        FROM product p
        JOIN brand b ON p.brand_id = b.brand_id
        JOIN category c ON b.category_id = c.category_id
        WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(#{keyword}), '%')
           OR LOWER(b.brand_name) LIKE CONCAT('%', LOWER(#{keyword}), '%')
           OR LOWER(c.category_name) LIKE CONCAT('%', LOWER(#{keyword}), '%')
    """)
    List<Product> searchProductsByKeyword(@Param("keyword") String keyword);
}
