package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DiscountResponse {

    private String name;
    private BigDecimal discountPercent;
       private BigDecimal discountedPrice;


    public DiscountResponse() {

    }
}
