package com.project.E_Commerce.Controller.Cart;


import com.project.E_Commerce.Service.Cart.CartService;
import com.project.E_Commerce.UserDetails.CustomUserDetailsService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import com.project.E_Commerce.dto.Cart.CartRequest;
import com.project.E_Commerce.dto.Cart.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest request) {
        int currentUserId = getCurrentUserId();
        if(currentUserId!=request.getUserId())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: You can only access your own data");
        }
            cartService.addProductToCart(request);
            return ResponseEntity.ok("Product added to cart");
        }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFromCart( @PathVariable Integer userId, @PathVariable Integer productId) {
        int currentUserId = getCurrentUserId();
        if(currentUserId!=userId)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: You can only access your own data");
        }
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @PreAuthorize("hasRole('USER')")
  @GetMapping("/{userId}")
    public ResponseEntity<?> viewCart(@PathVariable("userId") int userId)
  {
      int currentUserId = getCurrentUserId();
      if(currentUserId!=userId)
      {
          return ResponseEntity.status(HttpStatus.FORBIDDEN)
                  .body("Access denied: You can only access your own data");
      }
      CartResponse cartResponse = cartService.viewCart(userId);
      return ResponseEntity.ok(cartResponse);

  }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Integer userId) {
        int currentUserId = getCurrentUserId();
        if(currentUserId!=userId)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: You can only access your own data");
        }
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart has been cleared successfully for user ID: " + userId);

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
