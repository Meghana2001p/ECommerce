package com.project.E_Commerce.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Adress
{

    private Integer id;
    private String type;
    private String street;
    private String city;
    private String zipCode;
    private  Integer userId;//ManyToOne
}
