package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Coupon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepo extends JpaRepository<Coupon,Integer> {

    @Query("select c from Coupon c where c.code =:code ")
    Optional<Object> findByCode(@Param("code") String code);


    @Query(
            value = "SELECT * FROM coupon WHERE expiry_date > CURRENT_TIMESTAMP",
            nativeQuery = true
    )
    List<Coupon> findActiveCoupons();

}
