package com.project.E_Commerce.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DiscountResponse {

    private String name;
    private BigDecimal discountPercent;
       private BigDecimal discountedPrice;


    public DiscountResponse() {

    }
}
