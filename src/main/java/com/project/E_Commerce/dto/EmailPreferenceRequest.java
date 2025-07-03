package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.UserEmailPreferences;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailPreferenceRequest {
    private UserEmailPreferences.EmailType emailType;
    private Boolean isSubscribed;
}

