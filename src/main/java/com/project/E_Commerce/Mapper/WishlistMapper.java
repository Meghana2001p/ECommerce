package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Wishlist;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WishlistMapper {

    // 1. Get all wishlist items by user
    @Select("SELECT * FROM wishlist WHERE user_id = #{userId}")
    List<Wishlist> getWishlistByUserId(@Param("userId") int userId);

    // 2. Add product to wishlist
    @Insert("INSERT INTO wishlist (user_id, product_id, created_at, available, product_name, product_image_url) " +
            "VALUES (#{userId}, #{productId}, #{createdAt}, #{available}, #{productName}, #{productImageUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertWishlistItem(Wishlist wishlist);

    // 3. Delete a wishlist item
    @Delete("DELETE FROM wishlist WHERE id = #{id}")
    void deleteWishlistItem(@Param("id") int id);

    // 4. Delete wishlist item by user and product
    @Delete("DELETE FROM wishlist WHERE user_id = #{userId} AND product_id = #{productId}")
    void deleteByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);

    // 5. Check if a product is already in wishlist
    @Select("SELECT * FROM wishlist WHERE user_id = #{userId} AND product_id = #{productId}")
    Wishlist getByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);
}
