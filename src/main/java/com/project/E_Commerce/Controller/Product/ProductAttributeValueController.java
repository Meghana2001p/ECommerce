package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Service.Product.ProductService;
import com.project.E_Commerce.dto.Product.ProductAttributeAssignmentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attributeValue")
public class ProductAttributeValueController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> addAttributeValue(@Valid @RequestBody ProductAttributeAssignmentRequest value) {
        return ResponseEntity.ok(productService.addAttributeValue(value));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateAttributeValue(@Valid @RequestBody ProductAttributeAssignmentRequest value
    ) {
        productService.updateAttributeValue(value);
        return ResponseEntity.ok("Updated Successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getAttributeValueById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttributeValue(@PathVariable("id") Integer id) {
                return ResponseEntity.ok(productService.deleteAttributeValue(id));
    }


}
