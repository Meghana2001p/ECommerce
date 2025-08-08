package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Entity.Cart.Cart;
import com.project.E_Commerce.Entity.Cart.CartItem;
import com.project.E_Commerce.Repository.Cart.CartItemRepo;
import com.project.E_Commerce.Service.Cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applyCoupon")
public class AppliedCouponController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemRepo cartItemRepo;

    @PostMapping("/{cartId}/{couponCode}")
    public ResponseEntity<String> applyCouponToCart(@PathVariable Integer cartId,@PathVariable String couponCode) {

      //System.out.println(cartItemRepo.getUserIdFromCartItem(cartId));
            cartService.applyCouponToCart(cartId, couponCode);
            return ResponseEntity.ok("Coupon applied successfully to cart");

    }
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeCouponFromCart(@PathVariable Integer cartId) {

            cartService.removeCouponFromCart(cartId);
            return ResponseEntity.ok("Coupon removed successfully from cart");

    }


}
