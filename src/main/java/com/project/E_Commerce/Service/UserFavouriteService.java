package com.project.E_Commerce.Service;

import com.project.E_Commerce.dto.WishlistResponse;

import java.util.List;

public interface UserFavouriteService {
    String addToFavourites(int userId, int productId);
    String removeFromFavourites(int userId, int productId);
    List<WishlistResponse> getFavouritedProductsByUserId(int userId);
}
