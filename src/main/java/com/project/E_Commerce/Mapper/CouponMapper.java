package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Coupon;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CouponMapper {

    // 1. Get coupon by code
    @Select("SELECT * FROM coupon WHERE code = #{code}")
    Coupon getByCode(@Param("code") String code);

    // 2. Get coupon by ID
    @Select("SELECT * FROM coupon WHERE id = #{id}")
    Coupon getById(@Param("id") int id);

    // 3. Insert new coupon
    @Insert("INSERT INTO coupon (code, discount_percent, expiry_date, usage_limit) " +
            "VALUES (#{code}, #{discountPercent}, #{expiryDate}, #{usageLimit})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCoupon(Coupon coupon);

    // 4. Update coupon usage limit or expiry
    @Update("UPDATE coupon SET discount_percent = #{discountPercent}, expiry_date = #{expiryDate}, usage_limit = #{usageLimit} " +
            "WHERE id = #{id}")
    void updateCoupon(Coupon coupon);

    // 5. Delete a coupon
    @Delete("DELETE FROM coupon WHERE id = #{id}")
    void deleteCoupon(@Param("id") int id);

    // 6. Get all coupons
    @Select("SELECT * FROM coupon ORDER BY expiry_date ASC")
    List<Coupon> getAllCoupons();


}
