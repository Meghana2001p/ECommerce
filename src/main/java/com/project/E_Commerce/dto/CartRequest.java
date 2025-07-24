package com.project.E_Commerce.dto;

import lombok.Data;

@Data
public class CartRequest {

    private  Integer userId;
    private Integer productId;
    private Integer quantity;

}
