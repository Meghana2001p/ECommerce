package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Coupon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepo extends JpaRepository<Coupon,Integer> {
    @Select("select c from Coupon c where c.code =:code ")
    Optional<Object> findByCode(@Param("code") String code);
}
