package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;


import java.math.BigDecimal;

public interface SearchResponse {
    Integer getProductId();
    String getName();
    BigDecimal getPrice();
    String getBrandName();
    String getImageUrl();
    Double getAverageRating();
}
