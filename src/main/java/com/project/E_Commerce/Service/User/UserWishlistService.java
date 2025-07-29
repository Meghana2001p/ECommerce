package com.project.E_Commerce.Service.User;

import com.project.E_Commerce.dto.User.WishlistResponse;

import java.util.List;

public interface UserWishlistService
{
    String addToWishlist(int userId, int productId);

    String  removeFromWishlist(int userId,int productId);

    List<WishlistResponse> getWishlistProductsByUserId(int userId);
}
