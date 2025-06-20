package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotification {

    private Integer id;

    @NotNull
    private EmailType type;

    @NotBlank
    @Size(max = 255)
    private String subject;

    @NotBlank
    private String bodyTemplate;

    private Boolean isHtml = true;

    public enum EmailType {
        ORDER_CONFIRMATION,
        PASSWORD_RESET,
        SHIPPING_UPDATE,
        PROMOTION,
        REFUND
    }
}
