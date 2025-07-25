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

    @Column(name = "code", nullable = false, unique = true, length = 255)
    private String code;


    @Column(name = "discount_amount", precision = 50, scale = 20)
    private BigDecimal discountAmount;


    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;


    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit;

    @Column(nullable = false)
    private Boolean isActive;
}
