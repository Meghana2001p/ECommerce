package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.OrderItem;
import com.project.E_Commerce.dto.UserOrderUpdateDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    // 1. Get all items for an order
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> getItemsByOrderId(@Param("orderId") int orderId);

    // 2. Insert a new item into an order
    @Insert("INSERT INTO order_item (order_id, product_id, quantity, price) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int  insertOrderItem(OrderItem orderItem);

    // 3. Get an order item by ID
    @Select("SELECT * FROM order_item WHERE id = #{id}")
    OrderItem getOrderItemById(@Param("id") int id);

    // 4. Delete order item by ID
    @Delete("DELETE FROM order_item WHERE id = #{id}")
    int deleteOrderItem(@Param("id") int id);

    // 5. Delete all items for an order
    @Delete("DELETE FROM order_item WHERE order_id = #{orderId}")
    int  deleteItemsByOrderId(@Param("orderId") int orderId);


    //update by the user
    @Update("""
    UPDATE orders
    SET 
        shipping_address = COALESCE(#{shippingAddress}, shipping_address),
        phone_number = COALESCE(#{phoneNumber}, phone_number),
        is_gift = COALESCE(#{isGift}, is_gift),
        updated_at = NOW(),
        order_status = CASE
            WHEN #{cancelOrder} = true AND order_status IN ('PENDING', 'PROCESSING') THEN 'CANCELLED'
            ELSE order_status
        END
    WHERE id = #{orderId}
""")
    int updateOrderByUser(UserOrderUpdateDto dto);

}
