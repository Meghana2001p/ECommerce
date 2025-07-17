package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Service.PaymentService;
import com.project.E_Commerce.dto.DiscountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController("/discount")
public class DiscountController {

    @Autowired
    private PaymentService paymentService;
    //Discount
    @PostMapping("/")
    public ResponseEntity<?> createDiscount(@RequestBody DiscountDto dto) {
        return ResponseEntity.ok(paymentService.createDiscount(dto));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getDiscountByCode(@PathVariable String code) {
        return ResponseEntity.ok(paymentService.getDiscountByCode(code));
    }

    @GetMapping("/active")
    public ResponseEntity<List<?>> getAllActiveDiscounts() {
        return ResponseEntity.ok(paymentService.getAllActiveDiscounts());
    }

    @PutMapping("/expire/{id}")
    public ResponseEntity<?> expireDiscount(@PathVariable Integer id) {
        paymentService.expireDiscount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateDiscount(@RequestParam String code, @RequestParam Integer userId) {
        return ResponseEntity.ok(paymentService.validateDiscount(code, userId));
    }



}
