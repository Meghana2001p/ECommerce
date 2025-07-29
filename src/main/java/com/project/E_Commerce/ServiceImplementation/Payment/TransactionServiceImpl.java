package com.project.E_Commerce.ServiceImplementation.Payment;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Payment.Payment;
import com.project.E_Commerce.Entity.Payment.Transaction;
import com.project.E_Commerce.Repository.Order.OrderRepo;
import com.project.E_Commerce.Repository.Payment.PaymentRepo;
import com.project.E_Commerce.Repository.Payment.TransactionRepo;
import com.project.E_Commerce.Service.Payment.TransactionService;
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

    @Autowired
    private PaymentRepo paymentRepo;

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


        Payment payment = paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for orderId: " + orderId));
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setTransactionId(transaction.getTransactionId());
        paymentRepo.save(payment);

        return transaction.getTransactionId();
    }

    @Override
    public Transaction getTransactionDetails(String transactionId) {
        return transactionRepo.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}
