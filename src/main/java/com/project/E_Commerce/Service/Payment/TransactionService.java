package com.project.E_Commerce.Service.Payment;

import com.project.E_Commerce.Entity.Payment.Transaction;

import java.math.BigDecimal;

public interface TransactionService {
    String generateTransaction(Integer orderId, BigDecimal amount);
    Transaction getTransactionDetails(String transactionId);
}
