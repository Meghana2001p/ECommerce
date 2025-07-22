package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Entity.Cart;
import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.dto.CartItemDto;
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



    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        Cart createdCart = cartService.createCart(cart);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }
    @PostMapping("/add-item")
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        CartItem addedItem = cartService.addCartItem(cartItem);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<List<CartItemDto>> getAllCartItemsByUserId(@PathVariable int userId) {
        List<CartItemDto> cartItems = cartService.getAllCartItemsById(userId);
        return ResponseEntity.ok(cartItems);
    }
    @DeleteMapping("/delete-item/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable int cartItemId) {
        String message = cartService.removeCartItem(cartItemId);
        return ResponseEntity.ok(message);
    }


}
