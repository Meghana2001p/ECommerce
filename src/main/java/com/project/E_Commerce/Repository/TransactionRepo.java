package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByTransactionId(String transactionId);
    Transaction  findByUserId(Integer userId);
}

