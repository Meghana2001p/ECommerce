package com.project.E_Commerce.Repository.Return;

import com.project.E_Commerce.Entity.Payment.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepo extends JpaRepository<Refund,Integer> {
    @Query("SELECT r FROM Refund r WHERE r.payment.order.user.id = :userId")
    List<Refund> findAllByPaymentOrderUserId(Integer userId);

}
