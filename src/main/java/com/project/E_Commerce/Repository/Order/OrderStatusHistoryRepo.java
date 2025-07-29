package com.project.E_Commerce.Repository.Order;

import com.project.E_Commerce.Entity.Order.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory,Integer> {
}
