package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Entity.Cart.Coupon;
import com.project.E_Commerce.Service.Cart.CartService;
import com.project.E_Commerce.dto.Product.CouponRequest;
import com.project.E_Commerce.dto.Product.CouponResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CartService cartService;

    @PostMapping("/")
    public ResponseEntity<Coupon> createCoupon(@RequestBody @Valid Coupon coupon) {
        Coupon savedCoupon = cartService.createCoupon(coupon);
        return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CouponResponse>> getAllAvailableCoupons() {
        List<CouponResponse> coupons = cartService.getAllAvailableCoupons();
        return ResponseEntity.ok(coupons);
    }


    @PutMapping("/{couponId}")
    public ResponseEntity<String> updateCouponById(
            @PathVariable Integer couponId,
            @RequestBody @Valid CouponRequest request) {

        cartService.updateCouponById(couponId, request);
        return ResponseEntity.ok("Coupon updated successfully");
    }


    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCouponById(@PathVariable Integer couponId) {
        cartService.deleteCouponById(couponId);
        return ResponseEntity.ok("Coupon deleted successfully");
    }



//    @PostMapping("/coupon/apply")
//    public ResponseEntity<AppliedCoupon> applyCoupon(@RequestBody @Valid AppliedCoupon appliedCoupon) {
//        AppliedCoupon applied = cartService.applyCoupon(appliedCoupon);
//        return new ResponseEntity<>(applied, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("/coupon/remove/{id}")
//    public ResponseEntity<String> removeAppliedCoupon(@PathVariable("id") int appliedCouponId) {
//        String result = cartService.removeAppliedCoupon(appliedCouponId);
//        return ResponseEntity.ok(result);
//    }
//    @DeleteMapping("/clear-cart/{userId}")
//    public ResponseEntity<String> clearCart(@PathVariable int userId) {
//        cartService.clearCart(userId);
//        return ResponseEntity.ok("Cart cleared successfully");
//    }
//    @GetMapping("/cart-summary/{userId}")
//    public ResponseEntity<CartAmountSummaryDto> getCartSummary(@PathVariable int userId) {
//        CartAmountSummaryDto summary = cartService.calculateCartSummary(userId);
//        return ResponseEntity.ok(summary);
//    }

}
