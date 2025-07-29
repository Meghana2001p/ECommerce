package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Integer> {
    @Query(
            value = "SELECT * FROM inventory WHERE product_id = :productId",
            nativeQuery = true
    )
    Optional<Inventory> findByProductId(@Param("productId") Integer productId);


}
