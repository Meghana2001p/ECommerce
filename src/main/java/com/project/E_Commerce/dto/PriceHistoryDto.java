package com.project.E_Commerce.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PriceHistoryDto {
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private LocalDateTime changedAt;
}