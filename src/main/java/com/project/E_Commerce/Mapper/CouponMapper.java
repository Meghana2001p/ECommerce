package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Cart.Coupon;
import com.project.E_Commerce.dto.Product.CouponResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public interface CouponMapper {
    @Mappings({
    })
    CouponResponse toCouponResponse(Coupon coupon);
}
