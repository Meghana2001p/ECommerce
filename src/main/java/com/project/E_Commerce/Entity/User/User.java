package com.project.E_Commerce.Entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email")
    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false, length = 255)
    private String password;

    @Transient
    private String confirmPassword;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be 10 digits and start with 6, 7, 8, or 9")
    private String phoneNumber;


    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;


    private boolean isActive;

    public enum Role {
        ADMIN, USER, SELLER,DELIVERY
    }

    public enum Status {
        ACTIVE, INACTIVE
    }



}
