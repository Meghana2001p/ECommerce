package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory,Integer> {
}
