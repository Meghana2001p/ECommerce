package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.AppliedCoupon;
import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.Entity.Coupon;

import java.math.BigDecimal;
import java.util.List;

//CartService
//Cart, CartItem, AppliedCoupon
public interface CartService {

    //what are all the things that happens in a cart
    //add to cart
    //rempove from cart
    //totall cart items and the total price will be shown
    //show available coupons
    //add coupon and show the discount price
    //remove coupon and remove the discount price



    // Core Cart Operations
    void addToCart(CartItem cartItem);
    void removeFromCart(int cartItemId);
    List<CartItem> getCartItemsByUserId(int userId);
    void clearCart(int userId);

    // Pricing & Discounts
    BigDecimal calculateTotalPrice(int userId);
    BigDecimal calculateDiscountedTotal(int userId);

    // Coupons
    List<Coupon> getAvailableCoupons(int userId);
    void applyCoupon(int userId, int couponId);
    void removeCoupon(int userId);
    AppliedCoupon getAppliedCoupon(int userId);

    // New Methods (as requested)
    boolean isProductAvailable(int productId, int requestedQuantity);
    boolean isCouponValid(int couponId, int userId);

}
