package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.Brand;
import com.project.E_Commerce.Service.Product.ProductService;
import com.project.E_Commerce.dto.Product.BrandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class BrandController {
    //Brand

    @Autowired
    private ProductService productService;

    @PostMapping("/brand/create-brand")
    public ResponseEntity<Brand> createBrand(@RequestBody BrandRequest brand) {
        Brand created = productService.createBrand(brand);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/brand/update/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody BrandRequest brand) {
        Brand updated = productService.updateBrand(id, brand);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id) {
        Brand brand = productService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/brand/all")
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(productService.getAllBrands());
    }

    @DeleteMapping("/brand/delete/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Integer id) {
        productService.deleteBrand(id);
        return ResponseEntity.ok("Brand deleted successfully");
    }

}
