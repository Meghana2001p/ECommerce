    package com.project.E_Commerce.Service;

    import com.project.E_Commerce.Entity.AppliedCoupon;
    import com.project.E_Commerce.Entity.Cart;
    import com.project.E_Commerce.Entity.CartItem;
    import com.project.E_Commerce.Entity.Coupon;
    import com.project.E_Commerce.dto.*;
    import jakarta.validation.Valid;

    import java.util.List;

    //CartService
    //Cart, CartItem, AppliedCoupon
    public interface CartService {

         //according to the entity
        //1st add the details in the cart
        //2nd add the details in the cartItem
        //3rd add teh coupon details
        //4th add the applied coupon


        void addProductToCart(CartRequest request);
        void removeProductFromCart(Integer userId, Integer productId);
        CartResponse viewCart(Integer userId);










       //what are all the things that happens in a cart
        //add to cart
        //rempove from cart
        //totall cart items and the total price will be shown
        //show available coupons
        //add coupon and show the discount price
        //remove coupon and remove the discount price
        Coupon createCoupon(Coupon coupon);
        List<CouponResponse>getAllAvailableCoupons();
        void updateCouponById(Integer couponId, @Valid CouponRequest request);
        void deleteCouponById(Integer couponId);

//        AppliedCoupon applyCoupon(AppliedCoupon appliedCoupon);
//        String removeAppliedCoupon(int appliedCoupon_Id);
//        void clearCart(int user_id);


        //calculating the price now
        CartAmountSummaryDto  calculateCartSummary(int userId);


    }
