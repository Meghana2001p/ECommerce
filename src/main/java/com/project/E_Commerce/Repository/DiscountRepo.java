package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepo  extends JpaRepository<Discount,Integer> {
}
