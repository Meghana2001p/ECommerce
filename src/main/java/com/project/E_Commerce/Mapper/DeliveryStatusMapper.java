package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.DeliveryStatus;
import com.project.E_Commerce.dto.DeliveryStatusAdminUpdateDto;
import com.project.E_Commerce.dto.DeliveryStatusAgentUpdateDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeliveryStatusMapper {

    // 1. Get delivery status by order ID
    @Select("SELECT * FROM delivery_status WHERE order_id = #{orderId}")
    DeliveryStatus getByOrderId(@Param("orderId") int orderId);

    // 2. Get delivery status by ID
    @Select("SELECT * FROM delivery_status WHERE id = #{id}")
    DeliveryStatus getById(@Param("id") int id);

    // 3. Insert new delivery status
    @Insert("INSERT INTO delivery_status (order_id, status, updated_at, tracking_number, carrier, estimated_delivery_date, delivery_type) " +
            "VALUES (#{orderId}, #{status}, #{updatedAt}, #{trackingNumber}, #{carrier}, #{estimatedDeliveryDate}, #{deliveryType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int  insertDeliveryStatus(DeliveryStatus deliveryStatus);

    // 4. Update status and timestamp
    @Update("UPDATE delivery_status SET status = #{status}, updated_at = CURRENT_TIMESTAMP WHERE order_id = #{orderId}")
    void updateStatus(@Param("orderId") int orderId, @Param("status") String status);

    // 5. Update tracking info (optional)
    @Update("UPDATE delivery_status SET tracking_number = #{trackingNumber}, carrier = #{carrier}, estimated_delivery_date = #{estimatedDeliveryDate} " +
            "WHERE order_id = #{orderId}")
    void updateTrackingInfo(DeliveryStatus deliveryStatus);

    // 6. Get all delivery records
    @Select("SELECT * FROM delivery_status ORDER BY updated_at DESC")
    List<DeliveryStatus> getAllDeliveryStatuses();



    @Update("""
        UPDATE delivery_status 
        SET 
            status = #{status},
            tracking_number = #{trackingNumber},
            estimated_delivery_date = #{estimatedDeliveryDate},
            updated_at = NOW()
        WHERE order_id = #{orderId}
    """)
    int updateDeliveryStatus(DeliveryStatusAgentUpdateDto dto);





    @Update("""
    UPDATE delivery_status 
    SET 
        status = #{status},
        tracking_number = #{trackingNumber},
        carrier = #{carrier},
        estimated_delivery_date = #{estimatedDeliveryDate},
        delivery_type = #{deliveryType},
        updated_at = NOW()
    WHERE order_id = #{orderId}
""")
    int updateDeliveryStatusByAdmin(DeliveryStatusAdminUpdateDto dto);


}
