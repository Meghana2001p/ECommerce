package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.UserFavourite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFavouriteMapper {

    // 1. Get all favourite products by user
    @Select("SELECT * FROM user_favourite WHERE user_id = #{userId} AND is_liked = TRUE")
    List<UserFavourite> getFavouritesByUserId(@Param("userId") int userId);

    // 2. Check if product is favourited by user
    @Select("SELECT * FROM user_favourite WHERE user_id = #{userId} AND product_id = #{productId}")
    UserFavourite getByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);

    // 3. Add product to favourites
    @Insert("INSERT INTO user_favourite (user_id, product_id, is_liked, added_at) " +
            "VALUES (#{userId}, #{productId}, #{isLiked}, #{addedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUserFavourite(UserFavourite userFavourite);

    // 4. Update like status (e.g., toggle)
    @Update("UPDATE user_favourite SET is_liked = #{isLiked} WHERE user_id = #{userId} AND product_id = #{productId}")
    void updateFavouriteStatus(@Param("userId") int userId, @Param("productId") int productId, @Param("isLiked") boolean isLiked);

    // 5. Remove product from favourites
    @Delete("DELETE FROM user_favourite WHERE user_id = #{userId} AND product_id = #{productId}")
    void deleteByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);
}
