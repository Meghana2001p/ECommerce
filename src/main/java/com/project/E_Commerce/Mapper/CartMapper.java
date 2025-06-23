package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Cart;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CartMapper {

    // 1. Get cart by user ID
    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    Cart getCartByUserId(@Param("userId") int userId);

    // 2. Get cart by cart ID
    @Select("SELECT * FROM cart WHERE cart_id = #{cartId}")
    Cart getCartById(@Param("cartId") int cartId);

    // 3. Create new cart for a user
    @Insert("INSERT INTO cart (user_id) VALUES (#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    int createCart(Cart cart);

    // 4. Delete cart by user ID
    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    int deleteCartByUserId(@Param("userId") int userId);
}
