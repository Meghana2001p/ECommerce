package com.project.E_Commerce.Service.Payment;


import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Payment.Payment;
import com.project.E_Commerce.dto.Payment.PaymentCreateRequestDto;
import com.project.E_Commerce.dto.Payment.PaymentResponseDto;
import com.project.E_Commerce.dto.Payment.UserPaymentSummaryDto;
import com.project.E_Commerce.dto.Product.DiscountRequest;
import com.project.E_Commerce.dto.Product.PriceHistoryDto;
import com.project.E_Commerce.dto.Product.ProductDiscountRequest;
import com.project.E_Commerce.dto.Product.ProductDiscountResponse;

import java.util.List;

public interface PaymentService {


    // Discount Management
    Discount createDiscount(DiscountRequest dto);

    List<Discount> getAllActiveDiscounts();

    void deleteDiscount(Integer id);

    // Product Discount Management
    void assignDiscountToProduct(ProductDiscountRequest request);

    ProductDiscountResponse getDiscountForProduct(Integer productId);

    void removeDiscountFromProduct(Integer productId);


}
