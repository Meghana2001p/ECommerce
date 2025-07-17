package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.ProductDiscount;
import com.project.E_Commerce.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/productDiscount")
public class ProductDiscountController {

//ProductDiscount
@Autowired
private PaymentService paymentService;

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
