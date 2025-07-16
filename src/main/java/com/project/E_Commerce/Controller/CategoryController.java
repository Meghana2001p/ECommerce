package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.Category;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.CategoryRequest;
import com.project.E_Commerce.dto.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/category")
public class CategoryController {
    //Category
    @Autowired
    private ProductService productService;

    // Create Category
    @PostMapping("/category/add")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest category) {
        Category created = productService.createCategory(category);
        return ResponseEntity.ok(created);
    }

    //  Update Category
    @PutMapping("/category/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        Category updated = productService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    // Get Category by ID
    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Category category = productService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    //Get All Categories
    @GetMapping("/category/all")
    public ResponseEntity<List<?>> getAllCategories() {
        List<CategoryResponse> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Delete Category
    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        productService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

}
