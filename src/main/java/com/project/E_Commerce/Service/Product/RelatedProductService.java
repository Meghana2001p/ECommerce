package com.project.E_Commerce.Service.Product;

import com.project.E_Commerce.dto.Product.RelatedProductRequest;
import com.project.E_Commerce.dto.Product.RelatedProductResponse;

import java.util.List;

public interface RelatedProductService {

    // Add a related product
    void addRelatedProduct(RelatedProductRequest request);

    // Get all related products for a specific product
    List<RelatedProductResponse> getRelatedProductsByProductId(Integer productId);

    // Remove a related product
    String removeRelatedProduct(Integer relatedProductId);
}
