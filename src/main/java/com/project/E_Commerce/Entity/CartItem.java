package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {

    private Integer id;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
}
