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

}
