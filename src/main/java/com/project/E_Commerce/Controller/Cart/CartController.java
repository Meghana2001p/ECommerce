package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Entity.Cart;
import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.dto.CartItemDto;
import com.project.E_Commerce.dto.CartRequest;
import com.project.E_Commerce.dto.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


  //Cart is created as soon as the user Registers
    //adding the item to the cart the products and removing them and also showing the entire cart is something I have to do now

    @PostMapping("/")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest request) {
        cartService.addProductToCart(request);
        return ResponseEntity.ok("Product added to cart");
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFromCart(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }



  @GetMapping("/{userId}")
    public ResponseEntity<?> viewCart(@PathVariable("userId") int userId)
  {
      CartResponse cartResponse = cartService.viewCart(userId);
      return ResponseEntity.ok(cartResponse);

  }






}
