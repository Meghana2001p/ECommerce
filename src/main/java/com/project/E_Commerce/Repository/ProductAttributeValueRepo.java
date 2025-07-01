package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueRepo extends JpaRepository<ProductAttribute,Integer> {
}
