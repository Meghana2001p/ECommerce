package com.project.E_Commerce.Service.Product;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.dto.Product.ProductFilterRequest;

import java.util.List;

public interface ProductSearchService {
    List<Product> getFilteredProducts(ProductFilterRequest filter);

   void  validateFilterParameters(ProductFilterRequest filter);


}
