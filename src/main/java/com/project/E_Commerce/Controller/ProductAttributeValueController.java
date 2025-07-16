package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.ProductAttributeValue;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.ProductAttributeAssignmentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("attributeValue")
public class ProductAttributeValueController {

    //Product Attribute Value
    @Autowired
    private ProductService productService;

    // admin
    @PostMapping("/")
    public ResponseEntity<?> addAttributeValue(@Valid @RequestBody ProductAttributeAssignmentRequest value) {
        return ResponseEntity.ok(productService.addAttributeValue(value));
    }

    // admin
    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeValue> updateAttributeValue(
            @RequestParam Integer id,
            @Valid @RequestBody ProductAttributeValue value
    ) {
        return ResponseEntity.ok(productService.updateAttributeValue(id, value));
    }

    // admin
    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeValue> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getAttributeValueById(id));
    }

    //admin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttributeValue(@RequestParam Integer id) {
        return ResponseEntity.ok(productService.deleteAttributeValue(id));
    }


}
