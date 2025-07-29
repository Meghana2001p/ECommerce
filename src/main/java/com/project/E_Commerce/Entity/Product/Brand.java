package com.project.E_Commerce.Entity.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @NotNull(message = "Brand name cannot be null")
    @Column(name = "brand_name", nullable = false, unique = true)
    private String brandName;

    @NotNull(message = "Category list cannot be null")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "brand_category",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

}
