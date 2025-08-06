package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Service.Product.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;



    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{productId}/{quantity}")
    public ResponseEntity<?> updateStock(
            @PathVariable Integer productId,
            @PathVariable("quantity") Integer quantity) {

        log.info("Admin request: update stock of product {} → {}", productId, quantity);
        inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok("Data added"+quantity +" successfully");
    }
}
