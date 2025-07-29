    package com.project.E_Commerce.Service.Cart;

    import com.project.E_Commerce.Entity.Cart.Coupon;
    import com.project.E_Commerce.dto.*;
    import com.project.E_Commerce.dto.Cart.CartRequest;
    import com.project.E_Commerce.dto.Cart.CartResponse;
    import com.project.E_Commerce.dto.Product.CartAmountSummaryDto;
    import com.project.E_Commerce.dto.Product.CouponRequest;
    import com.project.E_Commerce.dto.Product.CouponResponse;
    import jakarta.validation.Valid;

    import java.util.List;

    //CartService
    //Cart, CartItem, AppliedCoupon
    public interface CartService {



        void addProductToCart(CartRequest request);
        void removeProductFromCart(Integer userId, Integer productId);
        CartResponse viewCart(Integer userId);



        Coupon createCoupon(Coupon coupon);
        List<CouponResponse>getAllAvailableCoupons();
        void updateCouponById(Integer couponId, @Valid CouponRequest request);
        void deleteCouponById(Integer couponId);




        void applyCouponToCart(Integer cartId, String couponCode);
        void removeCouponFromCart(Integer cartId);

        CartAmountSummaryDto calculateCartSummary(Integer id);


        void clearCart(Integer userId);
    }
