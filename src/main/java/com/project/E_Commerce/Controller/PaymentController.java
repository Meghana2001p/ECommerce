package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.Payment;
import com.project.E_Commerce.Service.PaymentService;
import com.project.E_Commerce.dto.PaymentRequestDto;
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


    @PostMapping("/make-payment")
    public ResponseEntity<Payment> makePayment(@RequestBody PaymentRequestDto dto) {
        Payment payment = paymentService.makePayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Integer orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Integer userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
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
}
