package com.project.E_Commerce.Service.Product;

import com.project.E_Commerce.dto.Product.RelatedProductRequest;
import com.project.E_Commerce.dto.Product.RelatedProductResponse;

import java.util.List;

public interface RelatedProductService {

    void addRelatedProduct(RelatedProductRequest request);

    List<RelatedProductResponse> getRelatedProductsByProductId(Integer productId);

    String removeRelatedProduct(Integer relatedProductId);
}
