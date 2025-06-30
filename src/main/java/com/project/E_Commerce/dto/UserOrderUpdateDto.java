package com.project.E_Commerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderUpdateDto {
    @NotNull
    private Integer orderId;

    private String newShippingAddress;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String newPhoneNumber;

    private Boolean isGift;

    private String giftMessage;

    private Boolean cancelOrder;
}
