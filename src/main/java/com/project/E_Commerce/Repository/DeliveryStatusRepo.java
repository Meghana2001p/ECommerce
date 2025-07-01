package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryStatusRepo extends JpaRepository<DeliveryStatus,Integer> {
}
