package com.project.E_Commerce.Service.User;

import com.project.E_Commerce.dto.User.WishlistResponse;

import java.util.List;

public interface UserFavouriteService {
    String addToFavourites(int userId, int productId);
    String removeFromFavourites(int userId, int productId);
    List<WishlistResponse> getFavouritedProductsByUserId(int userId);
}
