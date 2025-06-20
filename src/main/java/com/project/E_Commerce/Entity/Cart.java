package com.project.E_Commerce.Entity;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Integer id;
    @NotNull(message = "The user id should not be null")
    private Integer userId;



}
