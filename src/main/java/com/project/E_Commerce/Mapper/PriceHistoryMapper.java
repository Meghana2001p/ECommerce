package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.PriceHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PriceHistoryMapper {

    // 1. Get all price changes for a product
    @Select("SELECT * FROM price_history WHERE product_id = #{productId} ORDER BY changed_at DESC")
    List<PriceHistory> getPriceHistoryByProductId(@Param("productId") int productId);

    // 2. Get a price change record by ID
    @Select("SELECT * FROM price_history WHERE price_history_id = #{id}")
    PriceHistory getPriceHistoryById(@Param("id") int id);

    // 3. Insert a new price change record
    @Insert("INSERT INTO price_history (product_id, old_price, new_price, changed_at) " +
            "VALUES (#{productId}, #{oldPrice}, #{newPrice}, #{changedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "priceHistoryId")
    void insertPriceHistory(PriceHistory priceHistory);

    // 4. Delete a price history entry (optional)
    @Delete("DELETE FROM price_history WHERE price_history_id = #{id}")
    void deletePriceHistory(@Param("id") int id);

    // 5. Get all records
    @Select("SELECT * FROM price_history ORDER BY changed_at DESC")
    List<PriceHistory> getAllPriceHistories();
}
