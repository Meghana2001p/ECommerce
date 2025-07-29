package com.project.E_Commerce.ServiceImplementation.User;

import com.project.E_Commerce.Entity.Cart.Cart;
import com.project.E_Commerce.Entity.User.SearchHistory;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Entity.User.UserEmailPreferences;
import com.project.E_Commerce.Repository.Cart.CartRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.User.*;
import com.project.E_Commerce.Service.User.UserService;
import com.project.E_Commerce.dto.User.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

////User,UserFavourite,Wishlist,UserEmailPreference,SearchHistory
@Slf4j
@Service
public class UserServiceImplementation implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);



    @Autowired
    private UserFavouriteRepo userFavouriteRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;


   @Autowired
   private WishlistRepo wishlistRepo;


   @Autowired
   private UserEmailPreferencesRepo userEmailPreferencesRepo;

   @Autowired
   private SearchHistoryRepo searchHistoryRepo;

   @Autowired
   private CartRepo cartRepo;


    @Override
    public UserResponse createUser(User user) {

            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            if (user.getConfirmPassword() != null &&
                    !user.getPassword().equals(user.getConfirmPassword())) {
                throw new IllegalArgumentException("Password and confirm password do not match");
            }


            if (userRepo.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
            }


            //if the user status is null
            if (user.getStatus() == null) {
                user.setStatus(User.Status.ACTIVE);
            }
            if(user.isActive()==false)
            {
                user.setActive(true);
            }
            User createdUser = userRepo.save(user);
            Cart cart = new Cart();
            cart.setUser(createdUser);
            cart.setCreatedAt(LocalDateTime.now());
            cartRepo.save(cart);




            UserResponse userResponse= new UserResponse();

            userResponse.setStatus(createdUser.getStatus().name());
            userResponse.setUserId(createdUser.getId());
            userResponse.setName(createdUser.getName());
            userResponse.setRole(createdUser.getRole().name());
            userResponse.setEmail(createdUser.getEmail());
            userResponse.setPhoneNumber(createdUser.getPhoneNumber());


            if (createdUser.getId() == null && createdUser.getId() <= 0) {
                throw new IllegalArgumentException("Failed to create user");

            }
            return userResponse;

    }

    @Override
    public User loginUser(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

            Optional<User> user = userRepo.findByEmailAndPassword(email, password);
            if (user == null) {
                throw new RuntimeException("Invalid email or password");
            }
            return user.get();

    }

    @Override
    public String deactivateUser(Integer id) {

            Optional<User> existingUser = userRepo.findById(id);
            if (!existingUser.isPresent()) {
                throw new RuntimeException("User not found with ID: " + id);
            }

            User user = existingUser.get();

            if (user.isActive()==false) {
                throw new RuntimeException("User with ID " + id + " is already deactivated");
            }

            int result = userRepo.deactivateUser(id);
            if (result <= 0) {
                throw new RuntimeException("Failed to deactivate user with ID: " + id);

            }
            log.info("User with ID {} was successfully deactivated", id);
            return "SuccessFully deactivated the User";



    }

    @Override
    public String updateUserProfile(int userId, UserUpdateRequest dto) {

            Optional<User> optionalUser = userRepo.findById(userId);

            if (!optionalUser.isPresent()) {
                throw new RuntimeException("User not found with ID: " + userId);
            }

            User user = optionalUser.get();

            // Update name
            user.setName(dto.getName());

            // Update phone number
            user.setPhoneNumber(dto.getPhoneNumber());

            // Update password (if provided)
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                    throw new IllegalArgumentException("Password and confirm password do not match");
                }
            }

            userRepo.save(user);
            return "User profile updated successfully";


    }

    @Override
    public User updateUserByAdmin(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update");
        }

        // Check if user exists

            Optional<User> existingUserOpt = userRepo.findById(user.getId());
            if (!existingUserOpt.isPresent()) {
                throw new IllegalArgumentException("User with ID " + user.getId() + " not found");
            }


            // Run the JPQL update
            int result = userRepo.updateUserFields(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getRole(),
                    user.getStatus()
            );

            if (result <= 0) {
                throw new IllegalArgumentException("Failed to update user with ID: " + user.getId());
            }

            // Fetch updated user to return
            return userRepo.findById(user.getId()).orElseThrow(() ->
                    new IllegalArgumentException("User not found after update")
            );

    }


        @Override
    public void deleteUser(int userId) {


            Optional<User> user = userRepo.findById(userId);
            if (!user.isPresent()) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
            int deleteduser = userRepo.deleteByUserId(userId);
            if (deleteduser <= 0) {
                throw new IllegalArgumentException("Failed to delete user with ID " + userId);
            }
            log.info("User with ID {} deleted successfully", userId);


    }

    @Override
    public String changePassword(int userId, ChangePasswordRequest request) {

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            if (!request.getOldPassword().equals(user.getPassword())) {
                throw new IllegalArgumentException("Old password does not match");
            }

            if (request.getOldPassword().equals(request.getNewPassword())) {
                throw new IllegalArgumentException("New password must be different from old password");
            }

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new IllegalArgumentException("New password and confirm password do not match");
            }

            // Set the new plain password
            user.setPassword(request.getNewPassword());
            userRepo.save(user);

            log.info("Password changed for user ID {}", userId);
            return "Password changed successfully";

    }














    @Override
    public void createOrUpdateEmailPreferences(Integer userId, List<EmailPreferenceRequest> preferences) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        for (EmailPreferenceRequest dto : preferences) {
            // Check if the preference already exists for the user and email type
            Optional<UserEmailPreferences> existingPreferenceOpt =
                    userEmailPreferencesRepo.findByUserIdAndEmailType(userId, dto.getEmailType());

            if (existingPreferenceOpt.isPresent()) {
                // Update existing preference
                UserEmailPreferences preference = existingPreferenceOpt.get();
                preference.setIsSubscribed(dto.getIsSubscribed());
                userEmailPreferencesRepo.save(preference);
            } else {
                // Create new preference
                UserEmailPreferences newPreference = new UserEmailPreferences();
                newPreference.setUser(user);
                newPreference.setEmailType(dto.getEmailType());
                newPreference.setIsSubscribed(dto.getIsSubscribed());
                userEmailPreferencesRepo.save(newPreference);
            }
        }

    }




@Override
    public void clearAllPreferencesForUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userEmailPreferencesRepo.deleteByUserId(userId);
    }

    @Override
    public List<EmailPreferenceRequest> getPreferencesByUserId(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        List<UserEmailPreferences> preferences = userEmailPreferencesRepo.findAllByUserId(userId);

        return preferences.stream()
                .map(pref -> new EmailPreferenceRequest(pref.getEmailType(), pref.getIsSubscribed()))
                .collect(Collectors.toList());    }




    //Search History

    @Override
    public void saveSearchKeyword(SearchHistoryRequest request) {
        SearchHistory history = new SearchHistory();
        history.setUser(userRepo.findById(request.getUserId()).orElseThrow());
        if (request.getProductId() != null) {
            history.setProduct(productRepo.findById(request.getProductId()).orElse(null));
        }
        history.setKeyword(request.getKeyword());
        history.setSearchedAt(LocalDateTime.now());
        searchHistoryRepo.save(history);
    }

    @Override
    public List<SearchHistoryResponse> getSearchHistoryByUserId(Integer userId) {
        return searchHistoryRepo.findByUserIdOrderBySearchedAtDesc(userId).stream().map(h -> {
            SearchHistoryResponse resp = new SearchHistoryResponse();
            resp.setSearchId(h.getSearchId());
            resp.setKeyword(h.getKeyword());
            resp.setSearchedAt(h.getSearchedAt());
            if (h.getProduct() != null) {
                resp.setProductId(h.getProduct().getId());
                resp.setProductName(h.getProduct().getName());
            }
            return resp;
        }).toList();
    }

    @Override
    public void clearSearchHistory(Integer userId) {
        searchHistoryRepo.deleteByUserId(userId);
    }

    @Override
    public SearchHistoryResponse viewProductFromSearch(Integer searchId) {
        SearchHistory h = searchHistoryRepo.findById(searchId).orElseThrow();
        if (h.getProduct() == null) throw new RuntimeException("No product associated with this search.");
        SearchHistoryResponse resp = new SearchHistoryResponse();
        resp.setSearchId(h.getSearchId());
        resp.setKeyword(h.getKeyword());
        resp.setSearchedAt(h.getSearchedAt());
        resp.setProductId(h.getProduct().getId());
        resp.setProductName(h.getProduct().getName());
        resp. setProductImageUrl(h.getProduct().getImageAddress());
        return resp;
    }


}








