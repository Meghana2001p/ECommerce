package com.project.E_Commerce.Entity.Payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.E_Commerce.Entity.Product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_history_id")
    private Integer id;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Old price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Old price must be positive")
    @Column(name = "old_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal oldPrice;

    @NotNull(message = "New price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "New price must be positive")
    @Column(name = "new_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal newPrice;

    @PastOrPresent(message = "Change date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt = LocalDateTime.now();
}
