package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.*;
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

    //Admin add images
    @PostMapping("/Product-image/add")
    public ResponseEntity<String> addProductImage(@Valid @RequestBody ProductImage image) {
        String response = productService.addProductImage(image);
        return ResponseEntity.ok(response);
    }

    // Admin - Get All Images for a Product
    @GetMapping("/product-image/{id}")
    public ResponseEntity<List<ProductImage>> getImagesByProductId(@RequestParam int productId) {
        List<ProductImage> images = productService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }

    //  Admin - Delete Product Image
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
    @PostMapping("/attribute-value/add")
    public ResponseEntity<ProductAttributeValue> addAttributeValue(@Valid @RequestBody ProductAttributeValue value) {
        return ResponseEntity.ok(productService.addAttributeValue(value));
    }

    // admin
    @PutMapping("/attribute-value/update/{id}")
    public ResponseEntity<ProductAttributeValue> updateAttributeValue(
            @RequestParam Integer id,
            @Valid @RequestBody ProductAttributeValue value
    ) {
        return ResponseEntity.ok(productService.updateAttributeValue(id, value));
    }

    // admin
    @GetMapping("/attribute-value/{id}")
    public ResponseEntity<ProductAttributeValue> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getAttributeValueById(id));
    }

    //admin
    @DeleteMapping("/attribute-value/delete/{id}")
    public ResponseEntity<String> deleteAttributeValue(@RequestParam Integer id) {
        return ResponseEntity.ok(productService.deleteAttributeValue(id));
    }


    //Brand


    @PostMapping("/brand/add")
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand created = productService.createBrand(brand);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/brand/update/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand brand) {
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

//Category

    // Create Category
    @PostMapping("/category/add")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
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
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Delete Category
    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        productService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }


//RelatedProduct

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


    //Product to display the list to the viewers








    //Product to display when u click on the product the detailes information





}
