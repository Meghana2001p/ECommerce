package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart,Integer> {
}
