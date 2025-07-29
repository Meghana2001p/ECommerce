package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.ProductDiscount;
import com.project.E_Commerce.Service.Payment.PaymentService;
import com.project.E_Commerce.dto.Product.ProductDiscountRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productDiscount")
public class ProductDiscountController {

//ProductDiscount
@Autowired
private PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<String> assignDiscountToProduct(@Valid @RequestBody ProductDiscountRequest request) {
        paymentService.assignDiscountToProduct(request);
        return ResponseEntity.ok("Discount assigned to product successfully");
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ProductDiscount> getDiscountForProduct(@PathVariable Integer productId) {
        ProductDiscount discount = paymentService.getDiscountForProduct(productId);
        return ResponseEntity.ok(discount);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeDiscountFromProduct(@PathVariable Integer productId) {
        paymentService.removeDiscountFromProduct(productId);
        return ResponseEntity.ok("Discount removed from product successfully");
    }



}
