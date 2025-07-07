package com.project.E_Commerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedProductDTO {
    private Integer id;
    private String name;
    private String thumbnailUrl;
    private BigDecimal price;
    private String relationshipType;
}
