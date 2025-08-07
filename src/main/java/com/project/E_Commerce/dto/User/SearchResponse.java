package com.project.E_Commerce.dto.User;

import java.math.BigDecimal;

public interface SearchResponse {
    Integer getProductId();
    String getName();
    BigDecimal getPrice();
    String getBrandName();
    String getImageUrl();
    Integer getAverageRating();
}
