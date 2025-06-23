package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    // 1. Get order by ID
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order getOrderById(@Param("id") int id);

    // 2. Get all orders by user
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY order_date DESC")
    List<Order> getOrdersByUserId(@Param("userId") int userId);

    // 3. Insert a new order
    @Insert("INSERT INTO orders (user_id, order_date, order_status, total_amount) " +
            "VALUES (#{userId}, #{orderDate}, #{orderStatus}, #{totalAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrder(Order order);

    // 4. Update order status
    @Update("UPDATE orders SET order_status = #{orderStatus} WHERE id = #{id}")
    int updateOrderStatus(@Param("id") int id, @Param("orderStatus") String orderStatus);

    // 5. Delete order (optional)
    @Delete("DELETE FROM orders WHERE id = #{id}")
    int  deleteOrder(@Param("id") int id);

    // 6. Get all orders (admin view)
    @Select("SELECT * FROM orders ORDER BY order_date DESC")
    List<Order> getAllOrders();
}
