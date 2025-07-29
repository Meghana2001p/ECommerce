package com.project.E_Commerce.Entity.User;

import com.project.E_Commerce.Entity.Product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "user_favourite",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
