package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory,Integer> {
}
