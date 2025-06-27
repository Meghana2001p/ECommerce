package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.AppliedCoupon;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppliedCouponMapper
{

    // 1. Get applied coupon by order ID
    @Select("SELECT * FROM applied_coupon WHERE order_id = #{orderId}")
    AppliedCoupon getByOrderId(@Param("orderId") int orderId);

    // 2. Get all orders where a specific coupon was applied
    @Select("SELECT * FROM applied_coupon WHERE coupon_id = #{couponId}")
    List<AppliedCoupon> getByCouponId(@Param("couponId") int couponId);

    // 3. Insert applied coupon
    @Insert("INSERT INTO applied_coupon (order_id, coupon_id) VALUES (#{orderId}, #{couponId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertAppliedCoupon(AppliedCoupon appliedCoupon);

    // 4. Delete applied coupon by ID
    @Delete("DELETE FROM applied_coupon WHERE id = #{id}")
    void deleteAppliedCoupon(@Param("id") int id);

    // 5. Delete by order ID (e.g. if order is cancelled)
    @Delete("DELETE FROM applied_coupon WHERE order_id = #{orderId}")
    void deleteByOrderId(@Param("orderId") int orderId);



    //verify the coupon is applied or not
   @Select("select * from the applied_coupon where id=#{id}")
    AppliedCoupon getAppliedCouponById(@Param("id") int id);


    @Select("SELECT * FROM applied_coupon WHERE cart_id = #{cartId}")
    AppliedCoupon getAppliedCouponByCartId(@Param("cartId") int cartId);
}
