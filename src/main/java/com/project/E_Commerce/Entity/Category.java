package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        name = "category",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category_name", "parent_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"brands", "parent"})

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be under 100 characters")
    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    // Self-referencing parent category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "category_id")
    private Category parent;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnoreProperties({"brands", "parent"})
    private List<Brand> brands;
}
