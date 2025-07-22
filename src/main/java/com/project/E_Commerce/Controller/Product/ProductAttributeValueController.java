package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.ProductAttributeAssignmentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attributeValue")
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
    @PutMapping("/")
    public ResponseEntity<?> updateAttributeValue(@Valid @RequestBody ProductAttributeAssignmentRequest value
    ) {
        productService.updateAttributeValue(value);
        return ResponseEntity.ok("Updated Successfully");
    }

    // admin
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getAttributeValueById(id));
    }

    //admin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttributeValue(@PathVariable("id") Integer id) {
            System.out.println("Deleting ID: " + id);
        return ResponseEntity.ok(productService.deleteAttributeValue(id));
    }


}
