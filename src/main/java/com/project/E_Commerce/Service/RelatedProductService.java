package com.project.E_Commerce.Service;

import com.project.E_Commerce.dto.RelatedProductRequest;
import com.project.E_Commerce.dto.RelatedProductResponse;

import java.util.List;

public interface RelatedProductService {

    // Add a related product
    void addRelatedProduct(RelatedProductRequest request);

    // Get all related products for a specific product
    List<RelatedProductResponse> getRelatedProductsByProductId(Integer productId);

    // Remove a related product
    String removeRelatedProduct(Integer relatedProductId);
}
