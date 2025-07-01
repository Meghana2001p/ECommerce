package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory,Integer> {
}
