package com.project.E_Commerce.Repository.Payment;

import com.project.E_Commerce.Entity.Payment.Payment;
import com.project.E_Commerce.dto.Payment.UserPaymentSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Integer> {

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    Optional<Payment> findByOrderId(Integer orderId);

   @Query(" SELECT p FROM Payment p WHERE p.order.user.id = :userId")
    List<Payment> findAllByOrderUserId(Integer userId);

}
