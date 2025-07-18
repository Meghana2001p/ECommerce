package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Cart;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {

    //    @Query("""
//        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
//        FROM CartItem c
//        WHERE c.cart.user.id = :userId AND c.product.id = :productId
//    """)
    boolean existsByUserId(@Param("userId") Integer userId);


    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Integer userId);

    @Query(value = """
                SELECT ci.product_id 
                FROM cart_item ci 
                JOIN cart c ON ci.cart_id = c.cart_id 
                WHERE c.user_id = :userId
            """, nativeQuery = true)
    Set<Integer> findProductIdsByUserId(@Param("userId") Integer userId);
}

