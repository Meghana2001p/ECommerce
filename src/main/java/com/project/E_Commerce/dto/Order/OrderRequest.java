package com.project.E_Commerce.dto.Order;

import com.project.E_Commerce.Entity.Order.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull
    private Integer userId;

    @NotNull(message = "Payment method is required")
    private Order.PaymentMethod paymentMethod;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    private Boolean isGift;           // true or false
}