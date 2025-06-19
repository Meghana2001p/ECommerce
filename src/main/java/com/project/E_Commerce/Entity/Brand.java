package com.project.E_Commerce.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    private  Integer brandId;
    private String brandName;
@ManyToOne
@JoinColumn(name = "categoryId",referencedColumnName = "categoryId")
    private Category category;


}
