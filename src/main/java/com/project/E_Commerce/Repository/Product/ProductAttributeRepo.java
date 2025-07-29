package com.project.E_Commerce.Repository.Product;

import com.project.E_Commerce.Entity.Product.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepo extends JpaRepository<ProductAttribute,Integer> {
}
