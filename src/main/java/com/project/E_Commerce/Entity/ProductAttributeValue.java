package com.project.E_Commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
        name = "product_attribute_value",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "attribute_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Attribute is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", nullable = false)
    private ProductAttribute attribute;

    @NotNull(message = "Value is required")
    @Size(max = 255, message = "Value must be under 255 characters")
    @Column(nullable = false, length = 255)
    private String value;
}
