package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon,Integer> {
}
