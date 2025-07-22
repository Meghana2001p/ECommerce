package com.project.E_Commerce.Service;

import java.util.List;

public interface UserWishlistService
{
    String addToWishlist(int userId, int productId);

    String  removeFromWishlist(int userId,int productId);

}
