package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Payment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentMapper {

    // 1. Get payment by ID
    @Select("SELECT * FROM payment WHERE id = #{id}")
    Payment getPaymentById(@Param("id") int id);

    // 2. Get payment by order ID
    @Select("SELECT * FROM payment WHERE order_id = #{orderId}")
    Payment getPaymentByOrderId(@Param("orderId") int orderId);

    // 3. Insert new payment record
    @Insert("INSERT INTO payment (order_id, payment_method, status, transaction_id, paid_at, amount) " +
            "VALUES (#{orderId}, #{paymentMethod}, #{status}, #{transactionId}, #{paidAt}, #{amount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPayment(Payment payment);

    // 4. Update payment status
    @Update("UPDATE payment SET status = #{status}, transaction_id = #{transactionId} WHERE id = #{id}")
    void updatePaymentStatus(@Param("id") int id, @Param("status") String status, @Param("transactionId") String transactionId);

    // 5. Delete payment record (if ever needed)
    @Delete("DELETE FROM payment WHERE id = #{id}")
    void deletePayment(@Param("id") int id);

    // 6. Get all payments (for admin or reports)
    @Select("SELECT * FROM payment ORDER BY paid_at DESC")
    List<Payment> getAllPayments();
}
