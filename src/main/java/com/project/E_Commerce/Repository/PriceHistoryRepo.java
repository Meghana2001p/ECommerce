package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceHistoryRepo extends JpaRepository<PriceHistory,Integer> {

    @Query("select p from PriceHistory p where p.product.id= : productId")
    List<PriceHistory> findByProductId(Integer productId);


    @Query("SELECT p.newPrice FROM PriceHistory p WHERE p.product.id = :productId AND DATE(p.changedAt) = :date")
    Optional<BigDecimal> findPriceAtDate(@Param("productId") Integer productId, @Param("date") LocalDateTime date);

}
