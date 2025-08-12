package com.project.E_Commerce.Controller.Cart;

import com.project.E_Commerce.Repository.Cart.CartItemRepo;
import com.project.E_Commerce.Repository.Cart.CartRepo;
import com.project.E_Commerce.Service.Cart.CartService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applyCoupon")
public class AppliedCouponController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepo cartRepo;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{cartId}/{couponCode}")
    public ResponseEntity<?> applyCouponToCart(@PathVariable Integer cartId,@PathVariable String couponCode)
    {
      Integer userId= cartRepo.findUserIdByCartId(cartId);
      if(userId!=getCurrentUserId())
      {
          throw new RuntimeException("User cannot access the cart");
      }
        cartService.applyCouponToCart(cartId, couponCode);
            return ResponseEntity.ok("Coupon applied successfully to cart");

    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> removeCouponFromCart(@PathVariable Integer cartId) {
        Integer userId= cartRepo.findUserIdByCartId(cartId);
        if(userId!=getCurrentUserId())
        {
            throw new RuntimeException("User cannot access the cart");
        }
            cartService.removeCouponFromCart(cartId);
            return ResponseEntity.ok("Coupon removed successfully from cart");

    }



    public int getCurrentUserId()
    {
        //get the authentication object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl)
        {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            return userDetails.getId();

        }
        throw new RuntimeException("Unauthorized access");
    }


}
