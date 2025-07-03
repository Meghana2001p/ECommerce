package com.project.E_Commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "product_attribute")
@Data
@AllArgsConstructor
@NoArgsConstructor

//size,color
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private int id;

    @NotBlank(message = "Attribute name is required")
    @Size(max = 100, message = "Attribute name must be under 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
}
