package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Payment;
import com.project.E_Commerce.dto.UserPaymentSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Integer> {

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    Optional<Payment> findByOrderId(Integer orderId);

   @Query(" SELECT p FROM Payment p WHERE p.order.user.id = :userId")
    List<Payment> findAllByOrderUserId(Integer userId);
    @Query("""
    SELECT new com.project.E_Commerce.dto.UserPaymentSummaryDto(
        u.name,
        o.id,
        p.transactionId,
      
        p.paidAt,
        pr.id,
        pr.name,
       pi.imageUrl,
        b.brandName,
        pr.price,
        oi.quantity
    )
    FROM Payment p   JOIN p.order o
    JOIN o.user u    JOIN OrderItem oi 
    ON oi.order.id = o.id
    JOIN oi.product pr     JOIN pr.brand b
    LEFT JOIN pr.images pi
    WHERE u.id = :userId
""")
    List<UserPaymentSummaryDto> findAllPaymentSummaryByUserId(@Param("userId") Integer userId);

}
