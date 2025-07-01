package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavouriteRepo extends JpaRepository<UserFavourite,Integer> {
}
