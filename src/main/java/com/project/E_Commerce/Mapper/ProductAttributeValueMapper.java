package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.ProductAttributeValue;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductAttributeValueMapper {

    //Create attribute value for a product
    @Insert("""
        INSERT INTO product_attribute_value (product_id, attribute_id, value)
        VALUES (#{productId}, #{attributeId}, #{value})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createAttributeValue(ProductAttributeValue attrValue);

    // Update attribute value
    @Update("""
        UPDATE product_attribute_value
        SET value = #{value}
        WHERE product_id = #{productId} AND attribute_id = #{attributeId}
    """)
    int updateAttributeValue(ProductAttributeValue attrValue);

    // Delete by product + attribute
    @Delete("""
        DELETE FROM product_attribute_value
        WHERE  id =#{id}
    """)
    int deleteAttributeValue(@Param("id") int id );

    // Get all attribute values for a product
    @Select("""
        SELECT * FROM product_attribute_value
        WHERE product_id = #{productId}
    """)
    List<ProductAttributeValue> getAttributeValuesByProduct(@Param("productId") int productId);

    //Get one attribute value by product and attribute
    @Select("""
        SELECT * FROM product_attribute_value
        WHERE product_id = #{productId} AND attribute_id = #{attributeId}
    """)
    ProductAttributeValue getOneByProductAndAttribute(@Param("productId") int productId,
                                                      @Param("attributeId") int attributeId);


    @Select("""
        SELECT * FROM product_attribute_value
        WHERE product_id = #{id}
    """)
    ProductAttributeValue getAttributeValueById(@Param("id")Integer id);
}
