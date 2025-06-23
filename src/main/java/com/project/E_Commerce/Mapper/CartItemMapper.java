package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemMapper {

    // 1. Get all items in a cart
    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId}")
    List<CartItem> getItemsByCartId(@Param("cartId") int cartId);

    // 2. Get a cart item by ID
    @Select("SELECT * FROM cart_item WHERE item_id = #{itemId}")
    CartItem getCartItemById(@Param("itemId") int itemId);

    // 3. Add item to cart
    @Insert("INSERT INTO cart_item (cart_id, product_id, quantity, price) " +
            "VALUES (#{cartId}, #{productId}, #{quantity}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    void insertCartItem(CartItem cartItem);

    // 4. Update quantity and price of an item
    @Update("UPDATE cart_item SET quantity = #{quantity}, price = #{price} WHERE item_id = #{itemId}")
    void updateCartItem(CartItem cartItem);

    // 5. Delete item from cart
    @Delete("DELETE FROM cart_item WHERE item_id = #{itemId}")
    void deleteCartItem(@Param("itemId") int itemId);

    // 6. Clear all items in a cart
    @Delete("DELETE FROM cart_item WHERE cart_id = #{cartId}")
    void deleteAllItemsByCartId(@Param("cartId") int cartId);

    // 7. Check if product already in cart
    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId} AND product_id = #{productId}")
    CartItem getCartItemByCartAndProduct(@Param("cartId") int cartId, @Param("productId") int productId);
}
