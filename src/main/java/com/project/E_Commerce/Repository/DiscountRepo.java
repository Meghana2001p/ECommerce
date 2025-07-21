package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Discount;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepo  extends JpaRepository<Discount,Integer>
{

  @Query("select d from Discount d where d.name=:name")
    Optional<Discount> findByName(String name);


    @Query("SELECT d FROM Discount d WHERE d.isActive = true AND d.endDate > :now")
    List<Discount> findAllActiveDiscounts(@Param("now") LocalDateTime now);

  @Query("SELECT d FROM Discount d WHERE d.isActive = true")
  List<Discount> findAllByIsActiveTrue();
  @Query(
          value = """
         SELECT * FROM discount 
         WHERE is_active = true 
         AND start_date <= CURRENT_TIMESTAMP 
         AND end_date >= CURRENT_TIMESTAMP
         """,
          nativeQuery = true
  )
  List<Discount> findActiveDiscounts();


    Optional<Discount> findByNameIgnoreCase(String name);

  List<Discount> findAllByIsActiveTrueAndEndDateBefore(LocalDateTime now);

 

  @Query(value = """
    SELECT d.discount_percent
    FROM discount d
    JOIN product_discount pd ON d.id = pd.discount_id
    WHERE pd.product_id = :productId
      AND d.is_active = true
      AND d.start_date <= NOW()
      AND d.end_date >= NOW()
    LIMIT 1
""", nativeQuery = true)
  Optional<Discount> findDiscountDetailsByProductId(Integer productId);







}
