package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Coupon;
import com.project.E_Commerce.dto.CouponResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface CouponMapper {
    @Mappings({
    })
    CouponResponse toCouponResponse(Coupon coupon);
}
