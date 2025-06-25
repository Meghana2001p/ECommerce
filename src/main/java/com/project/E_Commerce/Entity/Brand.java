package com.project.E_Commerce.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    private  Integer brandId;
    @NotNull
@Column(unique = true)
    private String brandName;

    @NotNull
@ManyToOne
@JoinColumn(name = "categoryId",referencedColumnName = "categoryId")
    private Category category;


}
