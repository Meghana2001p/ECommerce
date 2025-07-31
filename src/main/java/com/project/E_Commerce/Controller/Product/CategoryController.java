package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.Category;
import com.project.E_Commerce.Service.Product.ProductService;
import com.project.E_Commerce.dto.Product.CategoryRequest;
import com.project.E_Commerce.dto.Product.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/category")
public class CategoryController {
    //Category
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest category) {
        Category created = productService.createCategory(category);
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        Category updated = productService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Category category = productService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<?>> getAllCategories() {
        List<CategoryResponse> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        productService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

}
