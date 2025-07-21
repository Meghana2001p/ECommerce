package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Service.PaymentService;
import com.project.E_Commerce.dto.DiscountRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<?> createDiscount(@RequestBody @Valid DiscountRequest dto)
    {
        return ResponseEntity.ok(paymentService.createDiscount(dto));
    }


    @GetMapping("/active")
    public ResponseEntity<List<?>> getAllActiveDiscounts() {
        return ResponseEntity.ok(paymentService.getAllActiveDiscounts());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletDiscount(@PathVariable int id)
    {
           paymentService.deleteDiscount(id);
           return ResponseEntity.ok("deleted successfully");
    }


}
