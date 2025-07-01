package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
