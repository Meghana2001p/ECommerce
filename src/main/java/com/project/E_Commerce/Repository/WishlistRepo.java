package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Wishlist;
import com.project.E_Commerce.dto.WishlistResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WishlistRepo extends JpaRepository<Wishlist,Integer> {
    // 1. Get all wishlist items by user
    @Query("select w from WishlistMapper w.user.userId=:userId")
    List<Wishlist> findByUserUserId(@Param("userId") Integer userId);

    // 2. Insert is handled by save()

    // 3. Delete wishlist item by ID → handled by deleteById(id)

    // 4. Delete wishlist item by user and product
    @Transactional
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.user.userId = :userId AND w.product.id = :productId")
    void deleteByUserAndProduct(@Param("userId")Integer userId, @Param("productId")Integer productId);

    // 5. Check if product is already in wishlist
@Query("select w from Wishlist w where w.user.userId=:userId and w.product.id = :productId")
    Optional<Wishlist> findByUserUserIdAndProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);


    // ✅ Check if a wishlist entry exists
    @Query("SELECT COUNT(w) > 0 FROM Wishlist w WHERE w.user.userId = :userId AND w.product.id = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

    // ✅ Find wishlist entry for a user and product
    @Query("SELECT w FROM Wishlist w WHERE w.user.userId = :userId AND w.product.id = :productId")
    Optional<Wishlist> findByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

    // ✅ Get all wishlist entries for a user
    @Query("SELECT w FROM Wishlist w WHERE w.user.userId = :userId")
    List<Wishlist> findByUserUserId(@Param("userId") int userId);

    // ✅ Delete all wishlist entries for a user
    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.user.userId = :userId")
    void deleteByUserUserId(@Param("userId") int userId);

    @Query("""
    SELECT new com.project.E_Commerce.dto.WishlistProductResponse(
        p.id,
        p.name,
        p.imageAddress,
        p.description,
        p.price,
        p.isAvailable,
        b.name,
        w.createdAt
    )
    FROM Wishlist w
    JOIN w.product p
    JOIN p.brand b
    WHERE w.user.userId = :userId
""")
    List<WishlistResponse> findWishlistDetailsByUserId(@Param("userId") int userId);

}

