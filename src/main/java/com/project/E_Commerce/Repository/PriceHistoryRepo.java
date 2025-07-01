package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepo extends JpaRepository<PriceHistory,Integer> {
}
