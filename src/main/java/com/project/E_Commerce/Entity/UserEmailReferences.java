package com.project.E_Commerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailReferences
{

    private int id;
    private int userId;
    private String emailType;
    private boolean isSubscribed;

}
