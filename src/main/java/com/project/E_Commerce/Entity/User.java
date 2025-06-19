package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer userId;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private Role  role;
   public  enum Role
   {
        ADMIN,USER,SELLER;
   }
    private String status;


}
