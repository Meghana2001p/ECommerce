package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductAttributeRepo extends JpaRepository<ProductAttribute,Integer> {
}
