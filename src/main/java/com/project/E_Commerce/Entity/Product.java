package com.project.E_Commerce.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String imageAdress;
    private BigDecimal price;
    private  String sku;
    private boolean isAvaliable;
    @ManyToOne
    @JoinColumn(name="brand_id",referencedColumnName = "brandId")
    private Brand brand;

}
