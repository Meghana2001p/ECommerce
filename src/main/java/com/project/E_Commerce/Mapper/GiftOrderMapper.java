package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.GiftOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface GiftOrderMapper {

    // 1. Get gift order details by order ID
    @Select("SELECT * FROM gift_order WHERE order_id = #{orderId}")
    GiftOrder getByOrderId(@Param("orderId") int orderId);

    // 2. Insert gift order preferences
    @Insert("INSERT INTO gift_order (order_id, gift_message, hide_price, gift_wrapping) " +
            "VALUES (#{orderId}, #{giftMessage}, #{hidePrice}, #{giftWrapping})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertGiftOrder(GiftOrder giftOrder);

    // 3. Update gift message or wrapping preferences
    @Update("UPDATE gift_order SET gift_message = #{giftMessage}, hide_price = #{hidePrice}, gift_wrapping = #{giftWrapping} " +
            "WHERE order_id = #{orderId}")
    void updateGiftOrder(GiftOrder giftOrder);

    // 4. Delete gift options by order ID
    @Delete("DELETE FROM gift_order WHERE order_id = #{orderId}")
    void deleteByOrderId(@Param("orderId") int orderId);
}
