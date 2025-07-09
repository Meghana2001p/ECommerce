package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.CartItem;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

    // Find CartItem by cart and product
    @Query("SELECT c FROM CartItem c WHERE c.cart.id = :cartId AND c.product.id = :productId")
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") Integer cartId, @Param("productId") Integer productId);

    // ✅ Find CartItems by userId through cart → user → userId
    @Query("SELECT c FROM CartItem c WHERE c.cart.user.id = :userId")
    List<CartItem> findByCartUserId(@Param("userId") Integer userId);

    // Find all CartItems in a cart
    @Query("SELECT c FROM CartItem c WHERE c.cart.id = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Integer cartId);
}
