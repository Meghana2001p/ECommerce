package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepo extends JpaRepository<Wishlist,Integer> {
}
