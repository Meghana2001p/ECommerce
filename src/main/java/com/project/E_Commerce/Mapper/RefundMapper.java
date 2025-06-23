package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Refund;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RefundMapper {

    // 1. Get refund by ID
    @Select("SELECT * FROM refund WHERE id = #{id}")
    Refund getById(@Param("id") int id);

    // 2. Get refunds by payment ID
    @Select("SELECT * FROM refund WHERE payment_id = #{paymentId}")
    List<Refund> getByPaymentId(@Param("paymentId") int paymentId);

    // 3. Insert refund
    @Insert("INSERT INTO refund (payment_id, amount, status, refunded_at) " +
            "VALUES (#{paymentId}, #{amount}, #{status}, #{refundedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRefund(Refund refund);

    // 4. Update refund status
    @Update("UPDATE refund SET status = #{status} WHERE id = #{id}")
    void updateRefundStatus(@Param("id") int id, @Param("status") String status);

    // 5. Get all refunds (admin/report)
    @Select("SELECT * FROM refund ORDER BY refunded_at DESC")
    List<Refund> getAllRefunds();
}
