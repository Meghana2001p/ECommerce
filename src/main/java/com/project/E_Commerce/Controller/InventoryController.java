package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    @Autowired
    private  InventoryService inventoryService;


    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateStock(
            @PathVariable Integer productId,
            @RequestParam("quantity") Integer quantity) {

        log.info("Admin request: update stock of product {} → {}", productId, quantity);
        inventoryService.updateStock(productId, quantity);
        return ResponseEntity.noContent().build();   // 204
    }
}
