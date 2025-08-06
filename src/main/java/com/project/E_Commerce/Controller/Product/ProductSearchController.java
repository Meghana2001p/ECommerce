package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Service.Product.ProductSearchService;
import com.project.E_Commerce.dto.Product.ProductFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productSearch")
public class ProductSearchController {
    @Autowired
   private ProductSearchService productSearchService;
    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestBody ProductFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    )

    {
        filter.setPage(page);
        filter.setSize(size);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        List<Product> products = productSearchService.getFilteredProducts(filter);
        return ResponseEntity.ok(products);
    }


}
