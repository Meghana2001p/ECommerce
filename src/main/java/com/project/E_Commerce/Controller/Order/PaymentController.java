package com.project.E_Commerce.Controller.Order;

import com.project.E_Commerce.Entity.Payment;
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




}
