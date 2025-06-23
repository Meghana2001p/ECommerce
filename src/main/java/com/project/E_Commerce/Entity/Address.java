package com.project.E_Commerce.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address
{

    private Integer id;

    @NotNull(message = "Address type is required")
    private AddressType type;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street must be under 255 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must be under 100 characters")
    private String city;

    @NotBlank(message = "Zip code cannot be blank")
    @Pattern(regexp = "\\d{5,10}", message = "Zip code must be between 5 and 10 digits")
    private String zipCode;

    @NotNull(message = "User ID is required")
    private Integer userId;

    public enum AddressType {
        HOME,
        WORK,
        BILLING,
        SHIPPING
    }
}
