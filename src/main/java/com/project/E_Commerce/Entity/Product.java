package com.project.E_Commerce.Entity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Collate;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Integer id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be under 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Image address is required")
    private String imageAddress;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must be under 50 characters")
    private String sku;

    private Boolean isAvailable = true;

    @NotNull(message = "Brand ID is required")
    @Column(unique = true)
    private Integer brandId;  // Foreign key for brand, used by MyBatis
}
