package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.CartItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
@Query("SELECT c \n" +
        "FROM CartItem c \n" +
        "WHERE c.cart.id = :cartId \n" +
        "  AND c.product.id = :productId\n")
    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);

@Query("SELECT c FROM CartItem c WHERE c.cart.user.userId = :userId")
    List<CartItem> findByCartUserId(Integer userId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Integer cartId);


}
