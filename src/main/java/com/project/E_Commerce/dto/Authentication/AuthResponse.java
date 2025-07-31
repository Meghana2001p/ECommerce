package com.project.E_Commerce.dto.Authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Integer userId;
    private String name;
    private String role;
    private String email;
    private String status;
    private String phoneNumber;
}
