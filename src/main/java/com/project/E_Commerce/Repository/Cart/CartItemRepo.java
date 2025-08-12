package com.project.E_Commerce.Repository.Cart;

import com.project.E_Commerce.Entity.Cart.CartItem;
import com.project.E_Commerce.dto.Cart.CartItemProjection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {


    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") Integer cartId, @Param("productId") Integer productId);
    List<CartItem> findByCartId(@Param("cartId") Integer cartId);


    @Query(value = "SELECT\n" +
            "    u.user_id AS userId,\n" +
            "    ci.item_id AS itemId,\n" +
            "    p.product_id AS productId,\n" +
            "    p.sku AS sku,\n" +
            "    p.name AS name,\n" +
            "    p.description AS description,\n" +
            "    p.price AS originalPrice,\n" +
            "    ci.quantity AS quantity,\n" +
            "    b.brand_name AS brandName,\n" +
            "    (\n" +
            "        SELECT pi.image_url\n" +
            "        FROM product_image pi\n" +
            "        WHERE pi.product_id = p.product_id\n" +
            "        LIMIT 1\n" +
            "    ) AS imageUrl,\n" +
            "    (\n" +
            "        SELECT d.discount_percent\n" +
            "        FROM product_discount pd\n" +
            "        JOIN discount d ON pd.discount_id = d.id\n" +
            "        WHERE pd.product_id = p.product_id\n" +
            "          AND d.is_active = 1\n" +
            "          AND NOW() BETWEEN d.start_date AND d.end_date\n" +
            "    ) AS discountPercent,\n" +
            "    (\n" +
            "        SELECT r.rating\n" +
            "        FROM review r\n" +
            "        WHERE r.product_id = p.product_id\n" +
            "        LIMIT 1\n" +
            "    ) AS productRating,\n" +
            "    c.id AS couponId,\n" +
            "    c.code AS couponCode,\n" +
            "    c.discount_amount AS couponDiscountAmount \n" +
            "FROM cart_item ci\n" +
            "JOIN cart crt ON ci.cart_id = crt.cart_id\n" +
            "JOIN product p ON ci.product_id = p.product_id\n" +
            "LEFT JOIN brand b ON p.brand_id = b.brand_id\n" +
            "JOIN users u ON crt.user_id = u.user_id\n" +
            "LEFT JOIN applied_coupon ac ON crt.cart_id = ac.cart_id\n" +
            "LEFT JOIN coupon c ON ac.coupon_id = c.id\n" +
            "WHERE crt.user_id = :userId\n",
            nativeQuery = true)
    List<CartItemProjection> getAllCartItems(@Param("userId") Integer userId);



    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM cart_item
    WHERE cart_id IN (
        SELECT cart_id FROM cart WHERE user_id = :userId
    )
""", nativeQuery = true)
    void clearCartByUserId(@Param("userId") Integer userId);


    @Query(value = "SELECT COUNT(*) FROM cart_item WHERE cart_id = :cartId", nativeQuery = true)
    Integer countByCartId(@Param("cartId") int cartId);

}
