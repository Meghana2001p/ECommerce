package com.project.E_Commerce.dto;

import java.math.BigDecimal;

public interface CartItemProjection {

    Integer getUserId();
    Integer getItemId();
    Integer getProductId();
    String getSku();
    String getName();
    String getDescription();
    BigDecimal getOriginalPrice();
    BigDecimal getCartItemPrice();
    Integer getQuantity();
    String getBrandName();
    String getImageUrl();
    BigDecimal getDiscountPercent();
    String getCouponCode();
    BigDecimal getCouponDiscountAmount();
    Double getProductRating();

}


//this is required  if ur using an sql and jpql queries then it is better to use these interface projects to fecth the data as
//it is supported by the spring and also helpful in the retrivin the data from the jpql or the sql