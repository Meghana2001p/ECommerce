package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.ReturnRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReturnRequestMapper {

    // 1. Get return request by ID
    @Select("SELECT * FROM return_request WHERE id = #{id}")
    ReturnRequest getById(@Param("id") int id);

    // 2. Get all return requests for a given order item
    @Select("SELECT * FROM return_request WHERE order_item_id = #{orderItemId}")
    List<ReturnRequest> getByOrderItemId(@Param("orderItemId") int orderItemId);

    // 3. Insert a new return request
    @Insert("INSERT INTO return_request (order_item_id, reason, status, requested_at) " +
            "VALUES (#{orderItemId}, #{reason}, #{status}, #{requestedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertReturnRequest(ReturnRequest returnRequest);

    // 4. Update status
    @Update("UPDATE return_request SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") int id, @Param("status") String status);

    // 5. Get all return requests (admin view)
    @Select("SELECT * FROM return_request ORDER BY requested_at DESC")
    List<ReturnRequest> getAllReturnRequests();
}
