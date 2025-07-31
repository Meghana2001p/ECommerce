package com.project.E_Commerce.Service.User;

import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.dto.User.*;

import java.util.List;

//User, UserEmailPreferences, UserFavourite, Wishlist, SearchHistory
public interface UserService {

    //user profile related-- create,login,update,get the profile
    UserResponse createUser(User user);

    String deactivateUser(Integer id);
     String activateUser(Integer id);


        String updateUserProfile(int userId, UserUpdateRequest dto);

    User updateUserByAdmin(UserAdminUpdateRequest user);

     void deleteUser(int userId);

    String changePassword(int userId, ChangePasswordRequest request);





    //EmailPreferences

     void createOrUpdateEmailPreferences(Integer userId, List<EmailPreferenceRequest> preferences);
     void clearAllPreferencesForUser(Integer userId);
    List<EmailPreferenceRequest> getPreferencesByUserId(Integer userId);




    void saveSearchKeyword(SearchHistoryRequest request);
    List<SearchHistoryResponse> getSearchHistoryByUserId(Integer userId);
    void clearSearchHistory(Integer userId);
    SearchHistoryResponse viewProductFromSearch(Integer searchId);
}