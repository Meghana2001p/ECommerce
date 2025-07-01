package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "related_product",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "related_product_id", "relationship_type"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Related product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_product_id", nullable = false)
    private Product relatedProduct;

    @NotNull(message = "Relationship type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private RelationshipType relationshipType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RelationshipType {
        SIMILAR,
        ACCESSORY,
        FREQUENTLY_BOUGHT_TOGETHER,
        RECOMMENDED
    }
}
