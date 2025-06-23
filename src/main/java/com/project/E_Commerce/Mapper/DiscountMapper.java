package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Discount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiscountMapper {

    // 1. Get all active discounts
    @Select("SELECT * FROM discount WHERE is_active = TRUE AND NOW() BETWEEN start_date AND end_date")
    List<Discount> getActiveDiscounts();

    // 2. Get discount by ID
    @Select("SELECT * FROM discount WHERE discount_id = #{discountId}")
    Discount getDiscountById(@Param("discountId") int discountId);

    // 3. Insert new discount
    @Insert("INSERT INTO discount (name, discount_percent, start_date, end_date, is_active) " +
            "VALUES (#{name}, #{discountPercent}, #{startDate}, #{endDate}, #{isActive})")
    @Options(useGeneratedKeys = true, keyProperty = "discountId")
    void insertDiscount(Discount discount);

    // 4. Update existing discount
    @Update("UPDATE discount SET name = #{name}, discount_percent = #{discountPercent}, " +
            "start_date = #{startDate}, end_date = #{endDate}, is_active = #{isActive} " +
            "WHERE discount_id = #{discountId}")
    void updateDiscount(Discount discount);

    // 5. Delete discount by ID
    @Delete("DELETE FROM discount WHERE discount_id = #{discountId}")
    void deleteDiscount(@Param("discountId") int discountId);

    // 6. Get all discounts
    @Select("SELECT * FROM discount ORDER BY start_date DESC")
    List<Discount> getAllDiscounts();
}
