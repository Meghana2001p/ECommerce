package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.AppliedCoupon;
import com.project.E_Commerce.Entity.CartItem;
import com.project.E_Commerce.Entity.Coupon;
import com.project.E_Commerce.Entity.User;
import com.project.E_Commerce.Exception.UserNotFoundException;
import com.project.E_Commerce.Mapper.*;
import com.project.E_Commerce.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService {

    //Cart, CartItem, AppliedCoupon

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private AppliedCouponMapper appliedCouponMapper;

    @Autowired
    private CouponMapper couponMapper;


      @Autowired
      private UserMapper userMapper;


     @Autowired
     private ProductMapper productMapper;

//add only if the product is inStock
    @Override
    public void addToCart(CartItem cartItem) {

       if( productMapper.inStock(cartItem.getProductId())) {
           int res = cartItemMapper.insertCartItem(cartItem);
       }

    }

    @Override
    public void removeFromCart(int cartItemId) {
  int res =  cartItemMapper.deleteCartItem(cartItemId);
    }

    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        return cartItemMapper.getItemsByCartId(userId);
    }

    @Override
    public void clearCart(int userId) {
      cartItemMapper.clearCartById(userId);
    }

    @Override
    public BigDecimal calculateTotalPrice(int userId) {
        return null;
    }

    @Override
    public BigDecimal calculateDiscountedTotal(int userId) {
        return null;
    }

    @Override
    public List<Coupon> getAvailableCoupons(int userId) {

       User user =  userMapper.getUserByID(userId);
       if(user!=null)

       {
           throw new UserNotFoundException("User doesnot exists");
       }
        return couponMapper.getAllCoupons();
    }

    @Override
    public void applyCoupon(int userId, int couponId) {
     appliedCouponMapper.insertAppliedCoupon(userId,couponId);
    }

    @Override
    public void removeCoupon(int userId) {

    }

    @Override
    public AppliedCoupon getAppliedCoupon(int userId) {
        return null;
    }

    @Override
    public boolean isProductAvailable(int productId, int requestedQuantity) {
        return false;
    }

    @Override
    public boolean isCouponValid(int couponId, int userId) {
        return false;
    }
}
