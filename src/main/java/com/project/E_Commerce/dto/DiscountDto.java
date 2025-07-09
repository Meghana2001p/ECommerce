package com.project.E_Commerce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiscountDto {
    private String code;
    private String description;
    private String type; // PERCENTAGE / FLAT
    private Double value;
    private LocalDateTime validFrom;
    private LocalDateTime validTill;
}
