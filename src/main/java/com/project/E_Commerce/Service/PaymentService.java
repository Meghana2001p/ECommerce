package com.project.E_Commerce.Service;


//Payment, Refund, PriceHistory, Discount, ProductDiscount

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.DiscountDto;
import com.project.E_Commerce.dto.PaymentRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {


    // Payment Management
    Payment makePayment(PaymentRequestDto dto);
    Payment getPaymentByOrderId(Integer orderId);
    List<Payment> getPaymentsByUserId(Integer userId);
    void updatePaymentStatus(Integer paymentId, Payment.PaymentStatus status);

    // Refund Management
    Refund initiateRefund(Integer orderId);
    Refund getRefundById(Integer refundId);
    List<Refund> getRefundsByUserId(Integer userId);
    void updateRefundStatus(Integer refundId, Refund.RefundStatus status);

    // Price History Tracking
    void recordPriceChange(Integer productId, Double newPrice);
    List<PriceHistory> getPriceHistory(Integer productId);
    Double getPriceAtDate(Integer productId, LocalDateTime date);

    // Discount Management
    Discount createDiscount(DiscountDto dto);
    Discount getDiscountByCode(String code);
    List<Discount> getAllActiveDiscounts();
    void expireDiscount(Integer id);
    boolean validateDiscount(String code, Integer userId);

    // Product Discount Management
    void assignDiscountToProduct(Integer productId, Integer discountId);
    ProductDiscount getDiscountForProduct(Integer productId);
    void removeDiscountFromProduct(Integer productId);
}
