package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.ProductAttribute;
import com.project.E_Commerce.Entity.ProductAttributeValue;
import com.project.E_Commerce.Entity.ProductImage;
import com.project.E_Commerce.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController
{

    @Autowired
    private ProductService productService;

    //admin
@PostMapping("/addProduct")
public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product)
{
Product addedProduct    =productService.addProduct(product);
return ResponseEntity.ok(addedProduct);
}

//admin update
    @PutMapping("/update-product")
    public ResponseEntity<?> updateProduct( int productID,   @Valid @RequestBody Product product)
    {
        Product updatedProduct    = productService.updateProduct(productID,product);
        return ResponseEntity.ok(updatedProduct);
    }

//admin delete

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct( @RequestParam  int productID)
    {
      String message =   productService.deleteProduct(productID);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/product/{id}")

    public ResponseEntity<?> getProductById(@RequestParam int productId) {
 Product product= productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }


//Product Image

    @PostMapping("/Product-image/add")
    public ResponseEntity<String> addProductImage(@Valid @RequestBody ProductImage image) {
        String response = productService.addProductImage(image);
        return ResponseEntity.ok(response);
    }

    // ✅ Admin - Get All Images for a Product
    @GetMapping("/product-image/{id}")
    public ResponseEntity<List<ProductImage>> getImagesByProductId(@RequestParam int productId) {
        List<ProductImage> images = productService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }

    // ✅ Admin - Delete Product Image
    @DeleteMapping("/product-image/delete/{id}")
    public ResponseEntity<String> deleteProductImage(@RequestParam int imageId) {
        String message = productService.deleteProductImage(imageId);
        return ResponseEntity.ok(message);
    }



    //Product Attributes

    //  Add attribute
    @PostMapping("/attribute/add")
    public ResponseEntity<ProductAttribute> addAttribute(@Valid @RequestBody ProductAttribute attribute) {
        return ResponseEntity.ok(productService.addAttribute(attribute));
    }

    // Update attribute
    @PutMapping("/attribute/update/{id}")
    public ResponseEntity<ProductAttribute> updateAttribute(
            @RequestParam int attributeId,
            @Valid @RequestBody ProductAttribute attribute
    ) {
        return ResponseEntity.ok(productService.updateAttribute(attributeId, attribute));
    }

    // Get by ID
    @GetMapping("/attribute/{attributeId}")
    public ResponseEntity<ProductAttribute> getAttribute(@PathVariable int attributeId) {
        return ResponseEntity.ok(productService.getAttributeById(attributeId));
    }

    // Delete
    @DeleteMapping("/attribute/delete/{attributeId}")
    public ResponseEntity<String> deleteAttribute(@RequestParam int attributeId) {
        return ResponseEntity.ok(productService.deleteAttribute(attributeId));
    }

    //Product Attribute Value



    // admin
    @PostMapping("/add")
    public ResponseEntity<ProductAttributeValue> addAttributeValue(@Valid @RequestBody ProductAttributeValue value) {
        return ResponseEntity.ok(productService.addAttributeValue(value));
    }

    // admin
    @PutMapping("/update")
    public ResponseEntity<ProductAttributeValue> updateAttributeValue(
            @RequestParam Integer id,
            @Valid @RequestBody ProductAttributeValue value
    ) {
        return ResponseEntity.ok(productService.updateAttributeValue(id, value));
    }

    // admin
    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeValue> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getAttributeValueById(id));
    }

    //admin
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAttributeValue(@RequestParam Integer id) {
        return ResponseEntity.ok(productService.deleteAttributeValue(id));
    }
}
