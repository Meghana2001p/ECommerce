package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Coupon code must not be blank")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    @Column(name = "code", nullable = false, unique = true, length = 255)
    private String code;

    @NotNull(message = "Discount percent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount cannot exceed 100%")
    @Column(name = "discount_percent", precision = 60, scale = 30, nullable = false)
    private BigDecimal discountPercent;

    @DecimalMin(value = "0.0", inclusive = false, message = "Discount amount must be greater than 0")
    @Column(name = "discount_amount", precision = 50, scale = 20)
    private BigDecimal discountAmount;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expiryDate;

    @NotNull(message = "Usage limit is required")
    @Min(value = 1, message = "Usage limit must be at least 1")
    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit;
}
