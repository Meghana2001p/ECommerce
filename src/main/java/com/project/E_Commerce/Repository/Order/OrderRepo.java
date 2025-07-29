package com.project.E_Commerce.Repository.Order;

import com.project.E_Commerce.Entity.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
}
