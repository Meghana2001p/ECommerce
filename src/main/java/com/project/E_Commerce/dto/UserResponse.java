package com.project.E_Commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String name;
    private  String role;
    private String email;
    private String status;
    private  String phoneNumber;

    public UserResponse(String name, String email, String role, String status,String phoneNumber) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.phoneNumber=phoneNumber;
    }
    public UserResponse(Integer userId,String name, String email, String role, String status,String phoneNumber) {
        this.userId=userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.phoneNumber=phoneNumber;
    }
}
