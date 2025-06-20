package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private Integer id;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must be under 500 characters")
    private String imageUrl;

    private Boolean isPrimary = false;

//    @Min(value = 0, message = "Sort order cannot be negative")
//    private Integer sortOrder = 0;

    @Size(max = 255, message = "Alt text must be under 255 characters")
    private String altText;
}
