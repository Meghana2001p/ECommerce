package com.project.E_Commerce.dto.Payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Integer orderID;

        private BigDecimal amount;
}
