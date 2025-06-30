package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.dto.CartItemDto;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface CartItemMapper {




    // 1. Get all items in a cart
    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId}")
    List<CartItem> getItemsByCartId(@Param("cartId") int cartId);

    // 2. Get a cart item by ID
    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId} LIMIT 1")
    CartItem getAnyCartItemByCartId(@Param("cartId") int cartId);


    // 3. Add item to cart
    @Insert("INSERT INTO cart_item (cart_id, product_id, quantity) " +
            "VALUES (#{cartId}, #{productId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    int insertCartItem(CartItem cartItem);

    // 4. Update quantity and price of an item
    @Update("UPDATE cart_item SET quantity = #{quantity}, price = #{price} WHERE item_id = #{itemId}")
    int updateCartItem(CartItem cartItem);

    // 5. Delete item from cart
    @Delete("DELETE FROM cart_item WHERE item_id = #{itemId}")
    int deleteOneCartItem(@Param("itemId") int itemId);

    // 6. Clear all items in a cart
    @Delete("DELETE FROM cart_item WHERE cart_id = #{cartId}")
    void deleteAllItemsByCartId(@Param("cartId") int cartId);

    // 7. Check if product already in cart
    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId} AND product_id = #{productId}")
    CartItem getCartItemByCartAndProduct(@Param("cartId") int cartId, @Param("productId") int productId);

    @Delete("DELETE FROM cart_item WHERE cart_id = #{cartId}")
    int clearCartByCartId(@Param("cartId") int cartId);




    @Select("""
    SELECT 
        ci.cart_id AS cartId,
        p.product_id AS productId,
        p.name AS productName,
        p.description,
        p.image_address AS imageUrl,
        ci.price,
        p.sku,
        p.is_available AS isAvailable,
        b.brand_name AS brandName,
        ci.quantity
            (ci.price * ci.quantity) AS total_price  
            
    FROM 
        cart_item ci
        
        JOIN
               
               cart c ON ci.cart_id = c.cart_id
    JOIN 
        
        product p ON ci.product_id = p.product_id
    JOIN 
        
        brand b ON p.brand_id = b.brand_id
    WHERE 
        ci.user_id = #{userId}
""")
    List<CartItemDto> getItemsByUserId(@Param("userId") int userId);







}
