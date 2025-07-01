package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDiscountRepo extends JpaRepository<ProductDiscount,Integer> {
}
