package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.RelatedProduct;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RelatedProductMapper {

    // 1. Get related products for a given product
    @Select("SELECT * FROM related_product WHERE product_id = #{productId} AND is_active = TRUE")
    List<RelatedProduct> getActiveRelatedProducts(@Param("productId") int productId);

    // 2. Get related products by type
    @Select("SELECT * FROM related_product WHERE product_id = #{productId} AND relationship_type = #{type} AND is_active = TRUE")
    List<RelatedProduct> getRelatedProductsByType(@Param("productId") int productId, @Param("type") String type);

    // 3. Insert new related product relation
    @Insert("INSERT INTO related_product (product_id, related_product_id, relationship_type, is_active, created_at) " +
            "VALUES (#{productId}, #{relatedProductId}, #{relationshipType}, #{isActive}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int  insertRelatedProduct(RelatedProduct relatedProduct);

    // 4. Deactivate a relationship
    @Update("UPDATE related_product SET is_active = FALSE WHERE id = #{id}")
    int deactivateRelation(@Param("id") int id);

    // 5. Delete by product and related_product
    @Delete("DELETE FROM related_product WHERE product_id = #{productId} AND related_product_id = #{relatedProductId} AND relationship_type = #{type}")
    int deleteRelation(@Param("productId") int productId, @Param("relatedProductId") int relatedProductId, @Param("type") String type);

    // 6. Get all relations
    @Select("SELECT * FROM related_product")
    List<RelatedProduct> getAllRelations();
    @Select("SELECT EXISTS(SELECT 1 FROM related_product WHERE id = #{id})")
    boolean relationExists(@Param("id") int id);
}
