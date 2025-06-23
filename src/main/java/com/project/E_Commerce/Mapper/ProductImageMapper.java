package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.ProductImage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductImageMapper {

    // 1. Get all images for a product
    @Select("SELECT * FROM product_image WHERE product_id = #{productId}")
    List<ProductImage> getImagesByProductId(@Param("productId") int productId);

    // 2. Get primary image for a product
    @Select("SELECT * FROM product_image WHERE product_id = #{productId} AND is_primary = TRUE LIMIT 1")
    ProductImage getPrimaryImage(@Param("productId") int productId);

    // 3. Insert a new product image
    @Insert("INSERT INTO product_image (product_id, image_url, is_primary, alt_text) " +
            "VALUES (#{productId}, #{imageUrl}, #{isPrimary}, #{altText})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertProductImage(ProductImage productImage);

    // 4. Update image metadata (optional)
    @Update("UPDATE product_image SET image_url = #{imageUrl}, is_primary = #{isPrimary}, alt_text = #{altText} " +
            "WHERE id = #{id}")
    void updateProductImage(ProductImage productImage);

    // 5. Delete image by ID
    @Delete("DELETE FROM product_image WHERE id = #{id}")
    void deleteProductImage(@Param("id") int id);

    // 6. Delete all images for a product
    @Delete("DELETE FROM product_image WHERE product_id = #{productId}")
    void deleteImagesByProductId(@Param("productId") int productId);

    // 7. Clear all primary images for a product (useful before setting a new one)
    @Update("UPDATE product_image SET is_primary = FALSE WHERE product_id = #{productId}")
    void clearPrimaryImages(@Param("productId") int productId);
}
