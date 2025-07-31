package com.project.E_Commerce.dto.User;

import com.project.E_Commerce.Entity.User.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAdminUpdateRequest {
    @NotNull
    private Integer id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String password;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull
    private User.Role role;

    @NotNull
    private User.Status status;
}

