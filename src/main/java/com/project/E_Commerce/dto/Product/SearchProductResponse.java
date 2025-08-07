package com.project.E_Commerce.dto.Product;

import jakarta.annotation.security.DenyAll;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductResponse {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageAddress;
    private boolean isAvailable;
    private String brandName;


}
