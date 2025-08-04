package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.ProductAttribute;
import com.project.E_Commerce.Service.Product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attribute")
public class ProductAttributeController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ProductAttribute> addAttribute(@Valid @RequestBody ProductAttribute attribute) {
        return ResponseEntity.ok(productService.addAttribute(attribute));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{attributeId}")
    public ResponseEntity<ProductAttribute> updateAttribute(@PathVariable int  attributeId,@Valid @RequestBody ProductAttribute attribute)
    {
        return ResponseEntity.ok(productService.updateAttribute(attributeId, attribute));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{attributeId}")
    public ResponseEntity<ProductAttribute> getAttribute(@PathVariable int attributeId)
    {
        return ResponseEntity.ok(productService.getAttributeById(attributeId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{attributeId}")
    public ResponseEntity<String> deleteAttribute(@PathVariable int attributeId) {
        return ResponseEntity.ok(productService.deleteAttribute(attributeId));
    }

}
