package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.RelatedProduct;
import com.project.E_Commerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/relatedProduct")
public class RelatedProductController {
    //RelatedProduct
    @Autowired
    private ProductService productService;

    @PostMapping("/related-product/add")
    public ResponseEntity<RelatedProduct> createRelatedProduct(@RequestBody RelatedProduct relatedProduct) {
        return ResponseEntity.ok(productService.createRelatedProduct(relatedProduct));
    }

    @PutMapping("/related-product/update/{id}")
    public ResponseEntity<RelatedProduct> updateRelatedProduct(@PathVariable Integer id, @RequestBody RelatedProduct rp) {
        return ResponseEntity.ok(productService.updateRelatedProduct(id, rp));
    }

    @GetMapping("/related-product/{id}")
    public ResponseEntity<RelatedProduct> getRelatedProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getRelatedProductById(id));
    }

    @GetMapping("/related-product/all")
    public ResponseEntity<List<RelatedProduct>> getAllRelatedProduct() {
        return ResponseEntity.ok(productService.getAllRelatedProducts());
    }

    @DeleteMapping("/related-product/delete/{id}")
    public ResponseEntity<String> deleteRelatedProduct(@PathVariable Integer id) {
        productService.deleteRelatedProduct(id);
        return ResponseEntity.ok("Deleted successfully");
    }

}
