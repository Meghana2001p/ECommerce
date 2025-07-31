package com.project.E_Commerce.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;



    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be 10 digits and start with 6-9")
    private String phoneNumber;

    private String email;
}
