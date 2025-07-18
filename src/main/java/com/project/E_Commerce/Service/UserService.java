package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.*;

import java.util.List;
import java.util.logging.Filter;

//User, UserEmailPreferences, UserFavourite, Wishlist, SearchHistory
public interface UserService {

    //user profile related-- create,login,update,get the profile
    UserResponse createUser(User user);

    User loginUser(String email, String password);

    String deactivateUser(Integer id);

    String updateUserProfile(int userId, UserUpdateRequest dto);

    User updateUserByAdmin(User user);

    public void deleteUser(int userId);

    String changePassword(int userId, ChangePasswordRequest request);


//UserFavourites

    // ✅ Add a product to user's favourites
    String addToFavourites(int userId, int productId);

    // ✅ Remove a product from user's favourites (soft delete)
    String removeFromFavourites(int userId, int productId);

    // ✅ Get all liked favourites for a user
    List<FavouriteProductResponse> getUserFavouritesResponse(int userId);







//User Wishlist

    String addToWishlist(int userId, int productId);

    String removeFromWishlist(int userId, int productId);

    List<WishlistResponse> getWishlistByUser(int userId);

    String clearWishlist(int userId);

    //EmailPreferences

     void createOrUpdateEmailPreferences(Integer userId, List<EmailPreferenceRequest> preferences);
     void clearAllPreferencesForUser(Integer userId);
    List<EmailPreferenceRequest> getPreferencesByUserId(Integer userId);




    void saveSearchKeyword(SearchHistoryRequest request);
    List<SearchHistoryResponse> getSearchHistoryByUserId(Integer userId);
    void clearSearchHistory(Integer userId);
    SearchHistoryResponse viewProductFromSearch(Integer searchId);
}