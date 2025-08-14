package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Service.Payment.PaymentService;
import com.project.E_Commerce.dto.Product.ProductDiscountRequest;
import com.project.E_Commerce.dto.Product.ProductDiscountResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productdiscount")
public class ProductDiscountController
{
//Product Discount
@Autowired
private PaymentService paymentService;

@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<String> assignDiscountToProduct(@Valid @RequestBody ProductDiscountRequest request) {
        paymentService.assignDiscountToProduct(request);
        return ResponseEntity.ok("Discount assigned to product successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<?> getDiscountForProduct(@PathVariable Integer productId) {
        ProductDiscountResponse discount = paymentService.getDiscountForProduct(productId);
        return ResponseEntity.ok(discount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeDiscountFromProduct(@PathVariable Integer productId) {
        paymentService.removeDiscountFromProduct(productId);
        return ResponseEntity.ok("Discount removed from product successfully");
    }
}
