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
public class RelatedProduct {

    private Integer id;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Related product ID is required")
    private Integer relatedProductId;

    @NotNull(message = "Relationship type is required")
    private RelationshipType relationshipType;

    private Boolean isActive = true;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RelationshipType {
        SIMILAR,
        ACCESSORY,
        FREQUENTLY_BOUGHT_TOGETHER,
        RECOMMENDED
    }
}
