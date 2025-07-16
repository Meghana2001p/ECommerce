package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.ProductAttribute;
import com.project.E_Commerce.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attribute")
public class ProductAttributeController {
    @Autowired
    private ProductService productService;
    //Product Attributes

    //  Add attribute
    @PostMapping("/attribute/add")
    public ResponseEntity<ProductAttribute> addAttribute(@Valid @RequestBody ProductAttribute attribute) {
        return ResponseEntity.ok(productService.addAttribute(attribute));
    }

    // Update attribute
    @PutMapping("/attribute/update/{attributeId}")
    public ResponseEntity<ProductAttribute> updateAttribute(
            @PathVariable int  attributeId,
            @Valid @RequestBody ProductAttribute attribute
    ) {
        return ResponseEntity.ok(productService.updateAttribute(attributeId, attribute));
    }

    // Get by ID
    @GetMapping("/attribute/{attributeId}")
    public ResponseEntity<ProductAttribute> getAttribute(@PathVariable int attributeId) {
        return ResponseEntity.ok(productService.getAttributeById(attributeId));
    }

    // Delete
    @DeleteMapping("/attribute/delete/{attributeId}")
    public ResponseEntity<String> deleteAttribute(@PathVariable int attributeId) {
        return ResponseEntity.ok(productService.deleteAttribute(attributeId));
    }
}
