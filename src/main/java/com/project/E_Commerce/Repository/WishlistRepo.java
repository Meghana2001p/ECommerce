package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Wishlist;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist,Integer> {
    // 1. Get all wishlist items by user
    @Query("select w from Wishlist w where  w.user.id=:userId")
    List<Wishlist> findByUserId(@Param("userId") Integer userId);

    // 2. Insert is handled by save()

    // 3. Delete wishlist item by ID → handled by deleteById(id)

    // 4. Delete wishlist item by user and product
    @Transactional
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :userId AND w.product.id = :productId")
    void deleteByUserAndProduct(@Param("userId")Integer userId, @Param("productId")Integer productId);

    // 5. Check if product is already in wishlist
@Query("select w from Wishlist w where w.user.id=:userId and w.product.id = :productId")
    Optional<Wishlist> findByUserIdAndProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);


    // ✅ Check if a wishlist entry exists
   // @Query("SELECT COUNT(w) > 0 FROM Wishlist w WHERE w.user.id = :userId AND w.product.id = :productId")
    boolean existsByUserId(@Param("userId") int userId);

    // ✅ Find wishlist entry for a user and product
//    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND w.product.id = :productId")
    Optional<Wishlist> findByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

    // ✅ Get all wishlist entries for a user
    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId")
    List<Wishlist> findByUserId(@Param("userId") int userId);

    // ✅ Delete all wishlist entries for a user
    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);




    @Query("""
    SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END
    FROM Wishlist w
    WHERE w.user.id = :userId AND w.product.id = :productId
""")
    boolean existsByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);


    @Query(value = "SELECT product_id FROM wishlist WHERE user_id = :userId", nativeQuery = true)
    Set<Integer> findProductIdsByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :userId AND w.product.id = :productId")
    int deleteByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}

