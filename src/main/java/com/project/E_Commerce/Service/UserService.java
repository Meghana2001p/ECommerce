package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.*;

import java.util.List;
import java.util.logging.Filter;

//User, UserEmailPreferences, UserFavourite, Wishlist, SearchHistory
public interface UserService {

    //user profile related-- create,login,update,get the profile
    User createUser(User user);
    User updateUser(User user);
    User getUserById(int userId);
    User userLogin(String email,String password);

    // Email Preferences--User can choose the type of email,update that and late get all the userEmails
    UserEmailPreferences setEmailPreference(UserEmailPreferences pref);
    void updateEmailSubscription(int userId, String emailType, boolean isSubscribed);
    List<UserEmailPreferences> getUserEmailPreferences(int userId);

    // Favorites (Likes)
    List<UserFavourite> getUserFavourites(int userId);
    UserFavourite getFavouriteByUserAndProduct(int userId, int productId);
    String addToFavourites(UserFavourite userFavourite);
    String updateFavouriteStatus(int userId, int productId, boolean isLiked);
    String removeFromFavourites(int userId, int productId);

    //Wishlist
    List<Wishlist> getWishlistByUserId(int userId);
    String addToWishlist(Wishlist wishlist);
    String removeWishlistItemById(int id);
    String removeWishlistItemByUserAndProduct(int userId, int productId);
    Wishlist getWishlistItem(int userId, int productId);



//searchHistory
void addSearchHistory(SearchHistory searchHistory);
    List<SearchHistory> getAllSearchHistory();
    List<SearchHistory> getSearchHistoryByUserId(int userId);
    List<SearchHistory> getSearchHistoryBySessionId(String sessionId);
    List<SearchHistory> getRecentSearchesForUser(int userId, int limit);



}
