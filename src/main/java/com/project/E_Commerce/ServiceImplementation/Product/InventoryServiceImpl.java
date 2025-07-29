package com.project.E_Commerce.ServiceImplementation.Product;

import com.project.E_Commerce.Entity.Product.Inventory;
import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Repository.Product.InventoryRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Service.Product.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public void updateStock(Integer productId, Integer newQuantity) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        if (newQuantity == null || newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity must be 0 or more");
        }
//
//
        Product product= productRepo.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
  if(product!=null) {
      Inventory inventory = new Inventory();
      inventory.setProduct(product);
      inventory.setStockQuantity(newQuantity);
      inventory.setInStock(newQuantity > 0);
      inventory.setLastUpdated(LocalDateTime.now());

      inventoryRepo.save(inventory);
  }


    }
}
