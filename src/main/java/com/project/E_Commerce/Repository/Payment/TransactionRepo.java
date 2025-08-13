package com.project.E_Commerce.Repository.Payment;

import com.project.E_Commerce.Entity.Payment.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByTransactionId(String transactionId);
    Transaction findByUserId(Integer userId);
  @Query(value="SELECT * FROM transactions WHERE order_id =:id",nativeQuery = true)
   Optional<Transaction> findByOrderId(Integer id);
}

