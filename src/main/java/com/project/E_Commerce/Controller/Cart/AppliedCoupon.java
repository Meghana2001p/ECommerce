package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applyCoupon")
public class AppliedCoupon {

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}/{couponCode}")
    public ResponseEntity<String> applyCouponToCart(@PathVariable Integer cartId,@PathVariable String couponCode) {
        try {
            cartService.applyCouponToCart(cartId, couponCode);
            return ResponseEntity.ok("Coupon applied successfully to cart");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeCouponFromCart(@PathVariable Integer cartId) {
        try {
            cartService.removeCouponFromCart(cartId);
            return ResponseEntity.ok("Coupon removed successfully from cart");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }

}
