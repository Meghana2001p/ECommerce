package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory.OrderStatus,Integer> {
}
