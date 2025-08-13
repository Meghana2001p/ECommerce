package com.project.E_Commerce.Service.Payment;


//Payment, Refund, PriceHistory, Discount, ProductDiscount

import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Payment.Payment;
import com.project.E_Commerce.Entity.Payment.Refund;
import com.project.E_Commerce.Entity.Product.ProductDiscount;
import com.project.E_Commerce.dto.Payment.PaymentCreateRequestDto;
import com.project.E_Commerce.dto.Payment.PaymentResponseDto;
import com.project.E_Commerce.dto.Payment.UserPaymentSummaryDto;
import com.project.E_Commerce.dto.Product.DiscountRequest;
import com.project.E_Commerce.dto.Product.PriceHistoryDto;
import com.project.E_Commerce.dto.Product.ProductDiscountRequest;
import com.project.E_Commerce.dto.Product.ProductDiscountResponse;
import com.project.E_Commerce.dto.Return.RefundResponseDto;

import java.util.List;

public interface PaymentService {


    // Payment Management
    PaymentResponseDto makePayment(PaymentCreateRequestDto dto);
    PaymentResponseDto getPaymentByOrderId(Integer orderId);
    List<UserPaymentSummaryDto> getPaymentsByUserId(Integer userId);
    void updatePaymentStatus(Integer paymentId, Payment.PaymentStatus status);



    // Price History Tracking
    void recordPriceChange(Integer productId, Double newPrice);
    List<PriceHistoryDto> getPriceHistory(Integer productId);
    Double getPriceAtDate(Integer productId);

    // Discount Management
    Discount createDiscount(DiscountRequest dto);
    List<Discount> getAllActiveDiscounts();
    void deleteDiscount(Integer id);





    // Product Discount Management
    void assignDiscountToProduct(ProductDiscountRequest request);
    ProductDiscountResponse getDiscountForProduct(Integer productId);
    void removeDiscountFromProduct(Integer productId);


}
