package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer userId;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String confirmPassword;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Status is required")
    private Status status;

    public enum Role {
        ADMIN, USER, SELLER
    }

    public enum Status {
        ACTIVE, INACTIVE, SUSPENDED, DELETED
    }
}
