package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.ProductDetail;
import com.project.E_Commerce.dto.ProductList;
import com.project.E_Commerce.dto.ProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest product)
{
Product addedProduct    =productService.addProduct(product);
return ResponseEntity.ok("Product added successfully");
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


    @GetMapping("/view/all")
    public ResponseEntity<List<ProductList>> getAllProductsForView(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<ProductList> result = productService.getAllAvailableProducts(pageable);
        return ResponseEntity.ok(result);
    }




    //Product to display when u click on the product the detailes information
    @GetMapping("/view/{productId}")
    public ResponseEntity<?> getProductDetailsById(@PathVariable Integer productId)
    {
        ProductDetail dto = productService.getProductDetailById(productId);
        return ResponseEntity.ok(dto);
    }



    //view when logged in
    @GetMapping("/view/user/{userId}")
    public ResponseEntity<?> getAllProductsAfterLogin(@PathVariable("userId") int userId
                                                      , @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size)
    {
        List<ProductList> result = productService.getAllProductsAfterLogin(userId,page);
        return ResponseEntity.ok(result);

    }


    //view when logged in
    @GetMapping("/view/user/{userId}/{productId}")
    public ResponseEntity<?> getProductDetailsByIdAndUserId(@PathVariable("userId") int userId
            , @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size,@PathVariable("productId") int productId)
    {
        ProductDetail result = productService.getProductByIdAndUserId(userId,page,productId);
        return ResponseEntity.ok(result);

    }

}
