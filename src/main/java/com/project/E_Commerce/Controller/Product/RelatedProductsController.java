package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Service.RelatedProductService;
import com.project.E_Commerce.dto.RelatedProductRequest;
import com.project.E_Commerce.dto.RelatedProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatedProduct")
public class RelatedProductsController {
    @Autowired
private RelatedProductService relatedProductService;

    @PostMapping("/")
    public ResponseEntity<?> addRelatedProduct(@RequestBody RelatedProductRequest request) {
        relatedProductService.addRelatedProduct(request);
        return ResponseEntity.ok("Related product added successfully.");
    }


    @GetMapping("/{productId}")
    public ResponseEntity<List<?>> getRelatedProducts(@PathVariable Integer productId) {
        List<RelatedProductResponse> relatedProducts = relatedProductService.getRelatedProductsByProductId(productId);
        return ResponseEntity.ok(relatedProducts);
    }


    @DeleteMapping("/{relatedProductId}")
    public ResponseEntity<String> removeRelatedProduct(
            @PathVariable Integer relatedProductId) {
       String message= relatedProductService.removeRelatedProduct(relatedProductId);
        return ResponseEntity.ok(message);
    }
}
