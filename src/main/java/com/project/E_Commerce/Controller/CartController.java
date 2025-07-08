package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.AppliedCoupon;
import com.project.E_Commerce.Entity.Cart;
import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.Entity.Coupon;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.dto.CartAmountSummaryDto;
import com.project.E_Commerce.dto.CartItemDto;
import com.project.E_Commerce.dto.CouponResponseDto;
import jakarta.validation.Valid;
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

    //coupon

    @PostMapping("/coupon/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody @Valid Coupon coupon) {
        Coupon savedCoupon = cartService.createCoupon(coupon);
        return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);
    }

    @GetMapping("/coupon/all-available")
    public ResponseEntity<List<CouponResponseDto>> getAllAvailableCoupons() {
        List<CouponResponseDto> coupons = cartService.getAllAvailableCoupons();
        return ResponseEntity.ok(coupons);
    }
    @PostMapping("/coupon/apply")
    public ResponseEntity<AppliedCoupon> applyCoupon(@RequestBody @Valid AppliedCoupon appliedCoupon) {
        AppliedCoupon applied = cartService.applyCoupon(appliedCoupon);
        return new ResponseEntity<>(applied, HttpStatus.CREATED);
    }

    @DeleteMapping("/coupon/remove/{id}")
    public ResponseEntity<String> removeAppliedCoupon(@PathVariable("id") int appliedCouponId) {
        String result = cartService.removeAppliedCoupon(appliedCouponId);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/clear-cart/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
    @GetMapping("/cart-summary/{userId}")
    public ResponseEntity<CartAmountSummaryDto> getCartSummary(@PathVariable int userId) {
        CartAmountSummaryDto summary = cartService.calculateCartSummary(userId);
        return ResponseEntity.ok(summary);
    }

}
