package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftOrder {

    private Integer id;  // Can be null if auto-generated

    @NotNull(message = "Order ID cannot be null")
    @Min(value = 1, message = "Order ID must be greater than 0")
    private Integer orderId;

    @Size(max = 500, message = "Gift message must not exceed 500 characters")
    private String giftMessage;

    private Boolean hidePrice = false;

    private Boolean giftWrapping = false;
}