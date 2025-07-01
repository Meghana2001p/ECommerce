package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment,Integer> {
}
