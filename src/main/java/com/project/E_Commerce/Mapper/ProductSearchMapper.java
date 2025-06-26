package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.dto.ProductFilterRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductSearchMapper {


    @Select("""
            <script>
                SELECT DISTINCT p.*
                FROM product p
                JOIN brand b ON p.brand_id = b.id
                WHERE 1 = 1
            
                <if test="minPrice != null">
                    AND p.price &gt;= #{minPrice}
                </if>
            
                <if test="maxPrice != null">
                    AND p.price &lt;= #{maxPrice}
                </if>
            
                <if test="brands != null and brands.size() > 0">
                    AND b.name IN
                    <foreach item="brand" collection="brands" open="(" separator="," close=")">
                        #{brand}
                    </foreach>
                </if>
            
                <if test="colors != null and colors.size() > 0">
                    AND EXISTS (
                        SELECT 1
                        FROM product_attribute_value pav
                        JOIN product_attribute pa ON pav.attribute_id = pa.id
                        WHERE pav.product_id = p.id
                        AND pa.name = 'Color'
                        AND pav.value IN
                        <foreach item="color" collection="colors" open="(" separator="," close=")">
                            #{color}
                        </foreach>
                    )
                </if>
            
                <if test="sizes != null and sizes.size() > 0">
                    AND EXISTS (
                        SELECT 1
                        FROM product_attribute_value pav
                        JOIN product_attribute pa ON pav.attribute_id = pa.id
                        WHERE pav.product_id = p.id
                        AND pa.name = 'Size'
                        AND pav.value IN
                        <foreach item="size" collection="sizes" open="(" separator="," close=")">
                            #{size}
                        </foreach>
                    )
                </if>
            
                <if test="keyword != null and keyword != ''">
                    AND (
                        LOWER(p.name) LIKE CONCAT('%', LOWER(#{keyword}), '%')
                        OR LOWER(p.description) LIKE CONCAT('%', LOWER(#{keyword}), '%')
                    )
                </if>
            
                <if test="sortBy != null and sortBy != ''">
                    ORDER BY
                    <choose>
                        <when test="sortBy == 'price'">
                            p.price
                        </when>
                        <when test="sortBy == 'name'">
                            p.name
                        </when>
                        <otherwise>
                            p.id
                        </otherwise>
                    </choose>
            
                    <choose>
                        <when test="sortDirection == 'desc'">DESC</when>
                        <otherwise>ASC</otherwise>
                    </choose>
                </if>
            
                LIMIT #{size} OFFSET #{page} * #{size}
            </script>
            """)
    List<Product> getFilteredProducts(ProductFilterRequest filter);
}