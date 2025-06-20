package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {


    private Integer id;
    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be positive")
    private Integer userId;

    @NotNull(message = "Product ID cannot be null")
    @Min(value = 1, message = "Product ID must be positive")
    private Integer productId;

    @PastOrPresent(message = "Created time can't be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt=LocalDateTime.now();

    private Boolean available = true;  // Whether the product is currently in stock or active
    private String productName;     // To reduce join if used in views
    private String productImageUrl; // For frontend wishlist display

}
