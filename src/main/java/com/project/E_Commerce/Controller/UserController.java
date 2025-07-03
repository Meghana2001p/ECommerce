package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.User;
import com.project.E_Commerce.Repository.UserRepo;
import com.project.E_Commerce.Service.UserService;
import com.project.E_Commerce.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


//User,UserFavourite,Wishlist,UserEmailPreference,SearchHistory
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
  private   UserService userService;

    @Autowired
    private UserRepo userRepo;

//user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user){

         User addedUser = userService.createUser(user);
        return  ResponseEntity.ok(addedUser);
    }

    //user
  @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody  @Valid LoginRequest req)
  {
      User user = userService.loginUser(req.getEmail(), req.getPassword());
      UserResponse resp = new UserResponse(
              user.getName(),
              user.getRole().name(),
              user.getEmail(),
              user.getStatus().name(),
              user.getPhoneNumber()
      );

      return ResponseEntity.ok(resp);
  }

  //admin
@GetMapping("/all-users")
public ResponseEntity<?> getAllUsers()
{
    List<User> users = userRepo.findAll();
    List<UserResponse> resp = users.stream()
            .map(u -> new UserResponse(
                    u.getUserId(),
                    u.getName(),
                    u.getEmail(),
                    u.getRole().name(),
                    u.getStatus().name(),
                    u.getPhoneNumber()
            ))
            .toList();
    return ResponseEntity.ok(resp);

}

//admin
@GetMapping("/user/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable("id") Integer id)
{
    Optional<User> user = userRepo.findById(id);
     User existingUser = user.get();
     UserResponse response=new UserResponse(existingUser.getName(),existingUser.getEmail(),existingUser.getRole().name(),existingUser.getStatus().name(),existingUser.getPhoneNumber());
     return  ResponseEntity.ok(response);

}

//admin
@PutMapping("/deactivate/{id}")
public ResponseEntity<?> deactivateUser(@PathVariable("id") Integer id)
{

   String message =  userService.deactivateUser(id);
   return  ResponseEntity.ok(message);

}

//user
@PutMapping("/update-profile/{id}")
public ResponseEntity<?> updateByUser( @PathVariable("id") Integer id, @RequestBody @Valid UserUpdateRequest user) {
  String message =  userService.updateUserProfile(id,user);
    return ResponseEntity.ok(message);
}

//admin
    @PutMapping("/admin/update-user")
    public ResponseEntity<?> updateUserByAdmin(@RequestBody @Valid User user) {
        User updatedUser = userService.updateUserByAdmin(user);
        return ResponseEntity.ok(updatedUser);
    }

//admin
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " deleted successfully");
    }



//user
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") int userId,
                                            @RequestBody @Valid ChangePasswordRequest request) {
        String message = userService.changePassword(userId, request);
        return ResponseEntity.ok(message);
    }






//UserFavourite  of the User all accessed by user
@PostMapping("/addFavourite/{userId}/{productId}")
public ResponseEntity<?> addFavourite(@PathVariable int userId, @PathVariable int productId) {
    String message = userService.addToFavourites(userId, productId);
    return ResponseEntity.ok(message);
}

    @DeleteMapping("/removeFavourite/{userId}/{productId}")
    public ResponseEntity<?> removeFavourite(@PathVariable int userId, @PathVariable int productId) {
        String message = userService.removeFromFavourites(userId, productId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/Favourites/{userId}")
    public ResponseEntity<List<FavouriteProductResponse>> getUserFavourites(@PathVariable Integer userId) {
        List<FavouriteProductResponse> favourites = userService.getUserFavouritesResponse(userId);
        return ResponseEntity.ok(favourites);
    }


//UserEmailPrefernces

    //create User Email preference
    @PostMapping("/email-preference/{userId}")
    public ResponseEntity<?> createOrUpdateEmailPreferences( @PathVariable("userId") Integer userId,
                                                             @RequestBody List<EmailPreferenceRequest> preferences){
         userService.createOrUpdateEmailPreferences(userId,preferences);
         return ResponseEntity.ok("User Preferences set successfully ");
    }

    @DeleteMapping("/clear/email-preference/{userId}")
    public ResponseEntity<?> clearAllPreferences(@PathVariable("userId") Integer userId) {
        userService.clearAllPreferencesForUser(userId);
        return ResponseEntity.ok("All email preferences cleared for user ID: " + userId);
    }

    @GetMapping("/get/email-preference/{userId}")
    public ResponseEntity<List<EmailPreferenceRequest>> getUserEmailPreferences(
            @PathVariable("userId") Integer userId) {

        List<EmailPreferenceRequest> preferences =
                userService.getPreferencesByUserId(userId);

        return ResponseEntity.ok(preferences);
    }


   //Seach history

    @PostMapping("/search-history/save")
    public ResponseEntity<String> saveSearchKeyword(@RequestBody SearchHistoryRequest request) {
        userService.saveSearchKeyword(request);
        return ResponseEntity.ok("Search keyword saved successfully.");
    }

    // Get recent search history for a user
    @GetMapping("/search-history/{userId}")
    public ResponseEntity<List<SearchHistoryResponse>> getSearchHistoryByUser(@PathVariable Integer userId) {
        List<SearchHistoryResponse> historyList = userService.getSearchHistoryByUserId(userId);
        return ResponseEntity.ok(historyList);
    }

    // Clear search history for a user
    @DeleteMapping("/search-history/clear/{userId}")
    public ResponseEntity<String> clearSearchHistory(@PathVariable Integer userId) {
        userService.clearSearchHistory(userId);
        return ResponseEntity.ok("Search history cleared successfully.");
    }

    // View product associated with a search entry
    @GetMapping("/search-history/view-product/{searchId}")
    public ResponseEntity<SearchHistoryResponse> viewProductFromSearch(@PathVariable Integer searchId) {
        SearchHistoryResponse response = userService.viewProductFromSearch(searchId);
        return ResponseEntity.ok(response);
    }

}
