package com.project.E_Commerce.Controller.Product;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Service.Product.ProductService;
import com.project.E_Commerce.UserDetails.CustomUserDetailsService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import com.project.E_Commerce.dto.Product.ProductDetail;
import com.project.E_Commerce.dto.Product.ProductList;
import com.project.E_Commerce.dto.Product.ProductRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController
{
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/")
public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest product)
{
Product addedProduct    =productService.addProduct(product);
return ResponseEntity.ok("Product added successfully");
}


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("productId") int productId,
            @Valid @RequestBody ProductRequest productRequest) {

        String updatedProduct = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productID}")
    public ResponseEntity<?> deleteProduct( @PathVariable  int productID)
    {
      String message =   productService.deleteProduct(productID);
        return ResponseEntity.ok(message);
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/view/user/{userId}")
    public ResponseEntity<?> getAllProductsAfterLogin(@PathVariable("userId") int userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, HttpServletRequest request)
    {
        int currentUserId = getCurrentUserId();
        if (currentUserId != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: You can only access your own data");
        }
        List<ProductList> result = productService.getAllProductsAfterLogin(userId,page);
        return ResponseEntity.ok(result);
    }


    //view when logged in
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/view/user/{userId}/{productId}")
    public ResponseEntity<?> getProductDetailsByIdAndUserId(@PathVariable("userId") int userId
            , @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size,@PathVariable("productId") int productId)
    {
        int currentUserId = getCurrentUserId();
        if (currentUserId != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: You can only access your own data");
        }
        ProductDetail result = productService.getProductByIdAndUserId(userId,page,productId);
        return ResponseEntity.ok(result);

    }

   public int getCurrentUserId()
   {
       //get the authentication object
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl)
       {
           UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
           return userDetails.getId();

       }
       throw new RuntimeException("Unauthorized access");
   }





}
