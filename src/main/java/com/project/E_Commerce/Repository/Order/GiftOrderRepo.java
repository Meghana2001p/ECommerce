package com.project.E_Commerce.Repository.Order;

import com.project.E_Commerce.Entity.Product.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftOrderRepo extends JpaRepository<GiftOrder,Integer> {
}
