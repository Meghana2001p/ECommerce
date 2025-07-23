package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.UserFavourite;
import com.project.E_Commerce.dto.FavouriteProductResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserFavouriteRepo extends JpaRepository<UserFavourite,Integer> {



    // 2. Check if product is favourited by user
    @Query("SELECT uf FROM UserFavourite uf WHERE uf.user.id = :userId AND uf.product.id = :productId")
    Optional<UserFavourite> findByUserIdAndProductId(Integer userId, Integer productId);



    List<UserFavourite> findByUserId(Integer userId);


    @Modifying
    @Transactional
    int deleteByUserIdAndProductId(Integer userId, Integer productId);

    @Query(value = "SELECT product_id FROM user_favourite WHERE user_id = :userId", nativeQuery = true)
    Set<Integer> findProductIdsByUserId(@Param("userId") Integer userId);

}
