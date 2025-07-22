package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.ProductImage;
import com.project.E_Commerce.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ProductImageController {
    //Product Image
    @Autowired
    private ProductService productService;

    //Admin add images
    @PostMapping("/Product-image/add")
    public ResponseEntity<String> addProductImage(@Valid @RequestBody ProductImage image) {
        String response = productService.addProductImage(image);
        return ResponseEntity.ok(response);
    }

    // Admin - Get All Images for a Product
    @GetMapping("/product-image/{id}")
    public ResponseEntity<List<ProductImage>> getImagesByProductId(@RequestParam int productId) {
        List<ProductImage> images = productService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }

    //  Admin - Delete Product Image
    @DeleteMapping("/product-image/delete/{id}")
    public ResponseEntity<String> deleteProductImage(@RequestParam int imageId) {
        String message = productService.deleteProductImage(imageId);
        return ResponseEntity.ok(message);
    }

}
