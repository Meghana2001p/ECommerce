package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.Brand;
import com.project.E_Commerce.Service.Product.ProductService;
import com.project.E_Commerce.dto.Product.BrandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {


    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Brand> createBrand(@RequestBody BrandRequest brand) {
        Brand created = productService.createBrand(brand);
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody BrandRequest brand) {
        Brand updated = productService.updateBrand(id, brand);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id) {
        Brand brand = productService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(productService.getAllBrands());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Integer id) {
        productService.deleteBrand(id);
        return ResponseEntity.ok("Brand deleted successfully");
    }

}
