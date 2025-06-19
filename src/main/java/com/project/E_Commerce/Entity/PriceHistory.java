package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory
{
    private Integer id;
    private Integer productId;
    private double oldPrice;
    private double newPrice;
    private LocalDateTime changedAt=LocalDateTime.now();
}
