package com.project.E_Commerce.Repository.Cart;

import com.project.E_Commerce.Entity.Cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {




    Optional<Cart> findByUserId(@Param("userId") Integer userId);

    Set<Integer> findProductIdsByUserId(@Param("userId") Integer userId);
}

