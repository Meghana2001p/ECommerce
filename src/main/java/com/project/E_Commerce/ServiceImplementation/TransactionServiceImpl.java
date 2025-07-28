package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Order;
import com.project.E_Commerce.Entity.Transaction;
import com.project.E_Commerce.Repository.OrderRepo;
import com.project.E_Commerce.Repository.TransactionRepo;
import com.project.E_Commerce.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public String generateTransaction(Integer orderId, BigDecimal amount) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setUserId(order.getUser().getId());
        transaction.setOrderId(orderId);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepo.save(transaction);
        return transaction.getTransactionId();
    }

    @Override
    public Transaction getTransactionDetails(String transactionId) {
        return transactionRepo.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}
