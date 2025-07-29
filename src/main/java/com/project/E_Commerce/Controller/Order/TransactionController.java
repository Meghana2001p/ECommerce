package com.project.E_Commerce.Controller.Order;

import com.project.E_Commerce.Entity.Payment.Transaction;
import com.project.E_Commerce.Service.Payment.TransactionService;
import com.project.E_Commerce.dto.Payment.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // 🔹 1. Generate Transaction ID for an Order
    @PostMapping("/pay")
    public ResponseEntity<?> generateTransaction(@RequestBody TransactionRequest transactionRequest) {

        String transactionId = transactionService.generateTransaction(transactionRequest.getOrderID(),transactionRequest.getAmount());
        return ResponseEntity.ok(transactionId);
    }

    // 🔹 2. Get Transaction Details by Transaction ID
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionDetails(
            @PathVariable String transactionId) {

        Transaction transaction = transactionService.getTransactionDetails(transactionId);
        return ResponseEntity.ok(transaction);
    }
}
