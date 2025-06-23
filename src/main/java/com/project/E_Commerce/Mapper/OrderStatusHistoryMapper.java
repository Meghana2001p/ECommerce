package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.OrderStatusHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderStatusHistoryMapper {

    // 1. Get all status changes for an order
    @Select("SELECT * FROM order_status_history WHERE order_id = #{orderId} ORDER BY updated_at ASC")
    List<OrderStatusHistory> getHistoryByOrderId(@Param("orderId") int orderId);

    // 2. Insert a new status update
    @Insert("INSERT INTO order_status_history (order_id, status, updated_at) " +
            "VALUES (#{orderId}, #{status}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertStatusHistory(OrderStatusHistory orderStatusHistory);

    // 3. Get latest status of an order (optional)
    @Select("SELECT * FROM order_status_history WHERE order_id = #{orderId} ORDER BY updated_at DESC LIMIT 1")
    OrderStatusHistory getLatestStatus(@Param("orderId") int orderId);

    // 4. Get all status changes (admin view)
    @Select("SELECT * FROM order_status_history ORDER BY updated_at DESC")
    List<OrderStatusHistory> getAllStatusChanges();
}
