package com.project.E_Commerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Integer orderID;
    private Integer userID;
    private BigDecimal amount;
}
