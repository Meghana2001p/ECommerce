package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}
