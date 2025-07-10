package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.Payment;
import com.project.E_Commerce.Entity.ProductDiscount;
import com.project.E_Commerce.Service.PaymentService;
import com.project.E_Commerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private  PaymentService paymentService;


    //Payment
    @PostMapping("/make-payment")
    public ResponseEntity<?> makePayment(@RequestBody PaymentCreateRequestDto dto) {
        PaymentResponseDto payment = paymentService.makePayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Integer orderId) {
        PaymentResponseDto payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPaymentsByUserId(@PathVariable Integer userId) {
        List<UserPaymentSummaryDto> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }


    @PutMapping("/status/{paymentId}")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam Payment.PaymentStatus status
    ) {
        paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok("Payment status updated to: " + status);
    }
 //Refund

    @PostMapping("/refund/initiate/{orderId}")
    public ResponseEntity<RefundResponseDto> initiateRefund(@PathVariable Integer orderId) {
        RefundResponseDto response = paymentService.initiateRefund(orderId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/refund/{refundId}")
    public ResponseEntity<RefundResponseDto> getRefundById(@PathVariable Integer refundId) {
        RefundResponseDto response = paymentService.getRefundById(refundId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/refund/user/{userId}")
    public ResponseEntity<List<RefundResponseDto>> getRefundsByUserId(@PathVariable Integer userId) {
        List<RefundResponseDto> refunds = paymentService.getRefundsByUserId(userId);
        return ResponseEntity.ok(refunds);
    }


    @PatchMapping("/refund/{refundId}/status")
    public ResponseEntity<RefundResponseDto> updateRefundStatus(
            @PathVariable Integer refundId,
            @RequestBody UpdateRefundStatusRequest request) {
        RefundResponseDto response = paymentService.updateRefundStatus(refundId, request.getStatus());
        return ResponseEntity.ok(response);
    }

    //Discount
    @PostMapping("/discounts")
    public ResponseEntity<?> createDiscount(@RequestBody DiscountDto dto) {
        return ResponseEntity.ok(paymentService.createDiscount(dto));
    }

    @GetMapping("/discounts/code/{code}")
    public ResponseEntity<?> getDiscountByCode(@PathVariable String code) {
        return ResponseEntity.ok(paymentService.getDiscountByCode(code));
    }

    @GetMapping("/discounts/active")
    public ResponseEntity<List<?>> getAllActiveDiscounts() {
        return ResponseEntity.ok(paymentService.getAllActiveDiscounts());
    }

    @PutMapping("/discounts/{id}/expire")
    public ResponseEntity<?> expireDiscount(@PathVariable Integer id) {
        paymentService.expireDiscount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/discounts/validate")
    public ResponseEntity<?> validateDiscount(@RequestParam String code, @RequestParam Integer userId) {
        return ResponseEntity.ok(paymentService.validateDiscount(code, userId));
    }





//ProductDiscount


    @PostMapping("/product-discount/assign")
    public ResponseEntity<String> assignDiscountToProduct(
            @RequestParam Integer productId,
            @RequestParam Integer discountId
    ) {
        paymentService.assignDiscountToProduct(productId, discountId);
        return ResponseEntity.ok("Discount assigned to product successfully");
    }


    @GetMapping("/product-discount/{productId}")
    public ResponseEntity<ProductDiscount> getDiscountForProduct(
            @PathVariable Integer productId
    ) {
        ProductDiscount discount = paymentService.getDiscountForProduct(productId);
        return ResponseEntity.ok(discount);
    }


    @DeleteMapping("/product-discount/remove/{productId}")
    public ResponseEntity<String> removeDiscountFromProduct(
            @PathVariable Integer productId
    ) {
        paymentService.removeDiscountFromProduct(productId);
        return ResponseEntity.ok("Discount removed from product successfully");
    }



}
