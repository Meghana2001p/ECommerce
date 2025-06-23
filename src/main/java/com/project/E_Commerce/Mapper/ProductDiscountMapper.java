package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.ProductDiscount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductDiscountMapper {

    // 1. Get all discounts for a given product
    @Select("SELECT * FROM product_discount WHERE product_id = #{productId}")
    List<ProductDiscount> getDiscountsByProductId(@Param("productId") int productId);

    // 2. Get all products associated with a discount
    @Select("SELECT * FROM product_discount WHERE discount_id = #{discountId}")
    List<ProductDiscount> getProductsByDiscountId(@Param("discountId") int discountId);

    // 3. Insert a product-discount link
    @Insert("INSERT INTO product_discount (product_id, discount_id) VALUES (#{productId}, #{discountId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertProductDiscount(ProductDiscount productDiscount);

    // 4. Delete by ID
    @Delete("DELETE FROM product_discount WHERE id = #{id}")
    int deleteById(@Param("id") int id);

    // 5. Delete by product and discount (optional)
    @Delete("DELETE FROM product_discount WHERE product_id = #{productId} AND discount_id = #{discountId}")
    int deleteByProductAndDiscount(@Param("productId") int productId, @Param("discountId") int discountId);

    // 6. Get all product-discount links
    @Select("SELECT * FROM product_discount")
    List<ProductDiscount> getAllProductDiscounts();
}
