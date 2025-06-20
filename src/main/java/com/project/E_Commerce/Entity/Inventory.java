package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    @PastOrPresent(message = "Last updated must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated=LocalDateTime.now();


    private Boolean inStock = true;


}
