package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Entity.Cart.Coupon;
import com.project.E_Commerce.Service.Cart.CartService;
import com.project.E_Commerce.dto.Product.CouponRequest;
import com.project.E_Commerce.dto.Product.CouponResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Coupon> createCoupon(@RequestBody @Valid Coupon coupon)
    {
        Coupon savedCoupon = cartService.createCoupon(coupon);
        return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CouponResponse>> getAllAvailableCoupons()
    {
        List<CouponResponse> coupons = cartService.getAllAvailableCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{couponId}")
    public ResponseEntity<String> updateCouponById(@PathVariable Integer couponId,@RequestBody @Valid CouponRequest request)
    {

        cartService.updateCouponById(couponId, request);
        return ResponseEntity.ok("Coupon updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCouponById(@PathVariable Integer couponId)
    {
        cartService.deleteCouponById(couponId);
        return ResponseEntity.ok("Coupon deleted successfully");
    }
}