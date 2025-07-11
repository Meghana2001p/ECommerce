package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Inventory;
import com.project.E_Commerce.Exception.DataBaseException;
import com.project.E_Commerce.Repository.InventoryRepo;
import com.project.E_Commerce.Service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public void updateStock(Integer productId, Integer newQuantity) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        if (newQuantity == null || newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity must be 0 or more");
        }
        try {
            Inventory inventory = inventoryRepo.findByProductId(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product ID: " + productId));

            inventory.setStockQuantity(newQuantity);
            inventory.setInStock(newQuantity > 0);
            inventory.setLastUpdated(LocalDateTime.now());

            inventoryRepo.save(inventory);

        } catch (DataAccessException dae) {
            log.error("Database error while updating stock for product {}: {}", productId, dae.getMessage(), dae);
            throw new DataBaseException("Internal server error");

        } catch (Exception ex) {
            log.error("Unexpected error in updateStock({}, {}):", productId, newQuantity, ex);
            throw new RuntimeException("Unexpected error while updating stock");
        }
    }
}
