package com.project.E_Commerce.Repository.Cart;


import com.project.E_Commerce.Entity.Cart.AppliedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppliedCouponRepo extends JpaRepository<AppliedCoupon,Integer> {

    // 1. Get applied coupon by order ID
    @Query("select a from AppliedCoupon a where a.order.id=:couponId")
    AppliedCoupon findByOrderId(Integer orderId);

    // 2. Get all orders where a specific coupon was applied
    @Query("select a from AppliedCoupon a where a.coupon.id=:couponId")
    List<AppliedCoupon> findByCouponId(@Param("couponId") Integer couponId);



    // 7. Delete by cart ID
    @Modifying
    @Query("DELETE FROM AppliedCoupon a WHERE a.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Integer cartId);

    Optional<AppliedCoupon> findByCartId(Integer cartId);


}
