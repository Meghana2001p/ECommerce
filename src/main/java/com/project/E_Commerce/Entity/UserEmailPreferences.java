package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailPreferences {

    private Integer id;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Email type is required")
    private EmailType emailType;

    private Boolean isSubscribed = true;

    public enum EmailType {
        ORDER_UPDATES,
        PROMOTIONS,
        PASSWORD_NOTIFICATIONS
    }
}
