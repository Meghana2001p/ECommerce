package com.project.E_Commerce.Entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Address
{
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "Address type is required")
    @Enumerated(EnumType.STRING)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public enum AddressType {
        HOME,
        WORK,
        BILLING,
        SHIPPING
    }


}
