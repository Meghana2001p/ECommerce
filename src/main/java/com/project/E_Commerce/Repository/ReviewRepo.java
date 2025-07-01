package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review,Integer> {
}
