package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.ProductImage;
import com.project.E_Commerce.Service.Product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ProductImageController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<String> addProductImage(@Valid @RequestBody ProductImage image) {
        String response = productService.addProductImage(image);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductImage(@PathVariable int id) {
        String message = productService.deleteProductImage(id);
        return ResponseEntity.ok(message);
    }

}
