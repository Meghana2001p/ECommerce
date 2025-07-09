package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Cart;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM CartItem c
        WHERE c.cart.user.id = :userId AND c.product.id = :productId
    """)
    boolean existsByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);


    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Integer userId);

}
