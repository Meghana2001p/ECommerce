package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Inventory;
import jakarta.persistence.LockModeType;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)

    @Select("select i from Inventory i where i.product.id= : productId")
    Optional<Inventory> findByProductId(Integer productId);
//🔒 Lock = Only one person can take stock at a time = ✅ safe stock deduction

}
