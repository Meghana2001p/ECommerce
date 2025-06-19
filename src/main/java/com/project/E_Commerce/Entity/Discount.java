package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    private Integer id;
    private String name;
    private float discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
}
