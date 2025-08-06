package com.project.E_Commerce.dto.Product;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDiscountResponse {
    private Integer productId;
    private String discountName;
    private BigDecimal discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
