package com.project.E_Commerce.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FavouriteProductResponse {


    private Integer productId;
    private String name;
    private String description;
    private String imageAddress;
    private BigDecimal price;
    private Boolean isAvailable;
    private String brandName;

}
