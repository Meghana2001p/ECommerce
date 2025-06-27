package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.AppliedCoupon;
import com.project.E_Commerce.Entity.Cart;
import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.Entity.Coupon;
import com.project.E_Commerce.dto.CartAmountSummaryDto;
import com.project.E_Commerce.dto.CartItemDto;
//import com.project.E_Commerce.dto.CartSummaryDto;

import java.math.BigDecimal;
import java.util.List;

//CartService
//Cart, CartItem, AppliedCoupon
public interface CartService {

     //according to the entity
    //1st add the details in the cart
    //2nd add the details in the cartItem
    //3rd add teh coupon details
    //4th add the applied coupon

    Cart addToCart(Cart cart);//just the id and teh userId
    CartItem addToCartItem(CartItem cartItem);//original Items into the cart
    List<CartItemDto>getAllCartItemsById(int user_id);
    String removeCartItem(int cart_item_id);


   //what are all the things that happens in a cart
    //add to cart
    //rempove from cart
    //totall cart items and the total price will be shown
    //show available coupons
    //add coupon and show the discount price
    //remove coupon and remove the discount price
    Coupon addCoupons(Coupon coupon);
    List<Coupon>getAllAvailableCoupons();
    AppliedCoupon ApplyCoupon(AppliedCoupon appliedCoupon);
    String removeCoupon(int appliedCoupon_Id);



    //calculating the price now
    CartAmountSummaryDto  calculateCartSummary(int userId);





}
