package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.AppliedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppliedCouponRepo extends JpaRepository<AppliedCoupon,Integer> {

    // 1. Get applied coupon by order ID
    AppliedCoupon findByOrderId(Integer orderId);

    // 2. Get all orders where a specific coupon was applied
    List<AppliedCoupon> findByCouponId(Integer couponId);

    // 3. Insert is handled by save()

    // 4. Delete applied coupon by ID is handled by deleteById()

    // 5. Verify the coupon is applied or not (by ID)
    Optional<AppliedCoupon> findById(Integer id);

    // 6. Get applied coupon by cart ID
    AppliedCoupon findByCartId(Integer cartId);

    // 7. Delete by cart ID
    void deleteByCartId(Integer cartId);

}
