package com.project.E_Commerce.Service;


//Payment, Refund, PriceHistory, Discount, ProductDiscount

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {


    // Payment Management
    PaymentResponseDto makePayment(PaymentCreateRequestDto dto);
    PaymentResponseDto getPaymentByOrderId(Integer orderId);
    List<UserPaymentSummaryDto> getPaymentsByUserId(Integer userId);
    void updatePaymentStatus(Integer paymentId, Payment.PaymentStatus status);

    // Refund Management
    RefundResponseDto initiateRefund(Integer orderId);
    RefundResponseDto getRefundById(Integer refundId);
    List<RefundResponseDto> getRefundsByUserId(Integer userId);
    RefundResponseDto updateRefundStatus(Integer refundId, Refund.RefundStatus status);

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
    ProductDiscount getDiscountForProduct(Integer productId);
    void removeDiscountFromProduct(Integer productId);


}
