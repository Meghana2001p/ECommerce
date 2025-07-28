package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.Transaction;
import com.project.E_Commerce.Repository.TransactionRepo;
import com.project.E_Commerce.dto.TransactionRequest;

import java.math.BigDecimal;

public interface TransactionService {
    String generateTransaction(Integer orderId, BigDecimal amount);
    Transaction getTransactionDetails(String transactionId);
}
