package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Inventory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InventoryMapper {

    // 1. Get inventory by ID
    @Select("SELECT * FROM inventory WHERE inventory_id = #{inventoryId}")
    Inventory getInventoryById(@Param("inventoryId") int inventoryId);

    // 2. Get inventory by product ID
    @Select("SELECT * FROM inventory WHERE product_id = #{productId}")
    Inventory getInventoryByProductId(@Param("productId") int productId);

    // 3. Get all inventory records
    @Select("SELECT * FROM inventory")
    List<Inventory> getAllInventory();

    // 4. Insert new inventory record
    @Insert("INSERT INTO inventory (product_id, stock_quantity, last_updated, in_stock) " +
            "VALUES (#{productId}, #{stockQuantity}, #{lastUpdated}, #{inStock})")
    @Options(useGeneratedKeys = true, keyProperty = "inventoryId")
    void insertInventory(Inventory inventory);

    // 5. Update stock quantity and in_stock status
    @Update("UPDATE inventory SET stock_quantity = #{stockQuantity}, " +
            "in_stock = #{inStock}, last_updated = CURRENT_TIMESTAMP " +
            "WHERE inventory_id = #{inventoryId}")
    void updateInventory(Inventory inventory);

    // 6. Delete inventory by ID
    @Delete("DELETE FROM inventory WHERE inventory_id = #{inventoryId}")
    void deleteInventory(@Param("inventoryId") int inventoryId);

    // 7. Reduce stock quantity after order
    @Update("UPDATE inventory SET stock_quantity = stock_quantity - #{quantity}, " +
            "last_updated = CURRENT_TIMESTAMP WHERE product_id = #{productId} " +
            "AND stock_quantity >= #{quantity}")
    int reduceStock(@Param("productId") int productId, @Param("quantity") int quantity);

    // 8. Check if product is in stock
    @Select("SELECT in_stock FROM inventory WHERE product_id = #{productId}")
    Boolean isProductInStock(@Param("productId") int productId);
}
