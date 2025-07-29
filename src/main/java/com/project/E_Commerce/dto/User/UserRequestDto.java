package com.project.E_Commerce.dto.User;

import lombok.Data;

@Data
public class UserRequestDto {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;
    private boolean active;
}
