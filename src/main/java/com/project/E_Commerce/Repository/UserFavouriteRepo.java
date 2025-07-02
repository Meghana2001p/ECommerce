package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.UserFavourite;
import com.project.E_Commerce.dto.FavouriteProductResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavouriteRepo extends JpaRepository<UserFavourite,Integer> {

    @Query("""
    SELECT new com.project.E_Commerce.dto.FavouriteProductDto(
        p.id,
        p.name,
        p.description,
        p.imageAddress,
        p.price,
        p.isAvailable,
        b.name
    )
    FROM UserFavourite uf
    JOIN uf.product p
    JOIN p.brand b
    WHERE uf.user.userId = :userId AND uf.isLiked = true
""")
    List<FavouriteProductResponse> findFavouriteProductDetailsByUserId(@Param("userId") Integer userId);


    // 2. Check if product is favourited by user
    @Query("SELECT uf FROM UserFavourite uf WHERE uf.user.userId = :userId AND uf.product.id = :productId")
    Optional<UserFavourite> findByUserIdAndProductId(Integer userId, Integer productId);

    // 3. Insert is handled by save() from JpaRepository

    // 4. Update like status (toggle)
    @Modifying
    @Query("UPDATE UserFavourite uf SET uf.isLiked = :isLiked WHERE uf.user.userId = :userId AND uf.product.id = :productId")
    int updateFavouriteStatus(@Param("userId") int userId, @Param("productId") int productId, @Param("isLiked") boolean isLiked);

    // 5. Delete by user and product
    @Modifying
    @Query("DELETE FROM UserFavourite uf WHERE uf.user.userId = :userId AND uf.product.id = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

}
