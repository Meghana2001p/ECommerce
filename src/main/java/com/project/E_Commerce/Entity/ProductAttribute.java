package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
//size,color all the
public class ProductAttribute
{
    private int id;

    @NotBlank(message = "Attribute name is required")
    @Size(max = 100, message = "Attribute name must be under 100 characters")
    private String name;


}
