package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.AppliedCoupon;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppliedCouponRepo extends JpaRepository<AppliedCoupon,Integer> {

    // 1. Get applied coupon by order ID
    @Query("select * from AppliedCoupon a where a.order.id=:couponId")
    AppliedCoupon findByOrderId(Integer orderId);

    // 2. Get all orders where a specific coupon was applied
    @Query("select * from AppliedCoupon a where a.coupon.id=:couponId")
    List<AppliedCoupon> findByCouponId(@Param("couponId") Integer couponId);

    // 3. Insert is handled by save()

    // 4. Delete applied coupon by ID is handled by deleteById()

    // 5. Verify the coupon is applied or not (by ID)
  //  Optional<AppliedCoupon> findById(Integer id);

    // 6. Get applied coupon by cart ID
    @Query("select * from AppliedCoupon a where a.cart.id=:cartId")
    AppliedCoupon findByCartId(@Param("cartId") Integer cartId);

    // 7. Delete by cart ID
    @Modifying
    @Query("DELETE FROM AppliedCoupon a WHERE a.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Integer cartId);

}
