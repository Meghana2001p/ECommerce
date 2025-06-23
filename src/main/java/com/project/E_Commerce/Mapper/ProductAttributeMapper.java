package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.ProductAttribute;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductAttributeMapper {

    //Create attribute
    @Insert("INSERT INTO product_attribute (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "attributeId")
    int createAttribute(ProductAttribute attribute);

    // Update attribute name
    @Update("UPDATE product_attribute SET name = #{name} WHERE attribute_id = #{attributeId}")
    int updateAttribute(ProductAttribute attribute);

    //Delete by ID
    @Delete("DELETE FROM product_attribute WHERE attribute_id = #{attributeId}")
    int deleteAttribute(@Param("attributeId") int attributeId);

    // Get by ID
    @Select("SELECT * FROM product_attribute WHERE attribute_id = #{attributeId}")
    ProductAttribute getAttributeById(@Param("attributeId") int attributeId);

    // Get all attributes
    @Select("SELECT * FROM product_attribute")
    List<ProductAttribute> getAllAttributes();

    //  Optional: Get by Name
    @Select("SELECT * FROM product_attribute WHERE name = #{name}")
    ProductAttribute getAttributeByName(@Param("name") String name);
}
