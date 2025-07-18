package com.project.E_Commerce.dto;

import java.math.BigDecimal;

public interface ProductWithBrandProjection {
    Integer getProductId();
    String getName();
    String getSku();
    BigDecimal getPrice();
    String getImageAddress();
    String getDescription();
    Boolean getIsAvailable();
    Integer getBrandId();
    String getBrandName();
}

