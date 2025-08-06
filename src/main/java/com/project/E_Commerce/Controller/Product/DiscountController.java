package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Service.Payment.PaymentService;
import com.project.E_Commerce.dto.Product.DiscountRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> createDiscount(@RequestBody @Valid DiscountRequest dto) {
        return ResponseEntity.ok(paymentService.createDiscount(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<List<?>> getAllActiveDiscounts() {
        return ResponseEntity.ok(paymentService.getAllActiveDiscounts());
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletDiscount(@PathVariable int id) {
        paymentService.deleteDiscount(id);
        return ResponseEntity.ok("deleted successfully");
    }


}
