package com.project.E_Commerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WishlistRequest {

    @NotNull(message = "User is required")
    private Integer userId;

    @NotNull(message = "Product is required")
    private Integer productId;
}
