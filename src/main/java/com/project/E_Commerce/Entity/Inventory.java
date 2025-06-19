package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory
{
    private Integer id;
    private Integer productId;
    private Integer stockQuantity;
    private LocalDateTime lastUpdated=LocalDateTime.now();
    private boolean inStock;


}
