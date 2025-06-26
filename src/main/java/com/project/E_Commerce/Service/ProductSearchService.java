package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.dto.ProductFilterRequest;

import java.util.List;

public interface ProductSearchService {
    List<Product> getFilteredProducts(ProductFilterRequest filter);

   void  validateFilterParameters(ProductFilterRequest filter);


}
