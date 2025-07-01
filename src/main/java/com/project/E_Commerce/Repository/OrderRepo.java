package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order,Integer> {
}
