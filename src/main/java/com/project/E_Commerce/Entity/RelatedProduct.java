package com.project.E_Commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Related product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_product_id", nullable = false)
    private Product relatedProduct;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private RelationshipType relationshipType;



    public enum RelationshipType {
        SIMILAR,
        ACCESSORY,
        FREQUENTLY_BOUGHT_TOGETHER,
        RECOMMENDED
    }
}
