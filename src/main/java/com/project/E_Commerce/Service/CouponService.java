package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.AppliedCoupon;
import com.project.E_Commerce.Entity.Coupon;
import com.project.E_Commerce.Entity.Discount;
import com.project.E_Commerce.dto.CartItemDto;
import com.project.E_Commerce.dto.CouponDto;
import com.project.E_Commerce.dto.DiscountDto;

import java.util.List;

//Coupon, Discount, AppliedCoupons
public interface CouponService {


    // 1. Coupon Management
    // --------------------------------------------
    Coupon createCoupon(CouponDto dto);
    Coupon updateCoupon(Integer couponId, CouponDto dto);
    void deleteCoupon(Integer couponId);
    Coupon getCouponByCode(String code);
    Coupon getCouponById(Integer id);
    List<Coupon> getAllCoupons();
    List<Coupon> getActiveCoupons();

    // --------------------------------------------
    // 2. Discount Management
    // --------------------------------------------
    Discount createDiscount(DiscountDto dto);
    Discount updateDiscount(Integer discountId, DiscountDto dto);
    void deleteDiscount(Integer discountId);
    Discount getDiscountById(Integer id);
    List<Discount> getAllDiscounts();

    // --------------------------------------------
    // 3. Coupon Application & Validation
    // --------------------------------------------
    AppliedCoupon applyCoupon(Integer userId, String couponCode, CartItemDto cartSummary);
    void removeAppliedCoupon(Integer userId);
    boolean isCouponValid(String couponCode, CartItemDto cartSummary);

    // --------------------------------------------
    // 4. Applied Coupon Tracking
    // --------------------------------------------
    List<AppliedCoupon> getAppliedCouponsByUser(Integer userId);
    boolean hasUserUsedCoupon(Integer userId, String couponCode);
    void recordAppliedCoupon(AppliedCoupon appliedCoupon);


    // 5. Smart Suggestions
    // --------------------------------------------
    List<Coupon> getEligibleCoupons(Integer userId, CartItemDto cartSummary);
    Coupon getBestApplicableCoupon(Integer userId, CartItemDto cartSummary);
}
