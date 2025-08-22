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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;


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
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

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
    public String deactivateUser(Integer id) {

            Optional<User> existingUser = userRepo.findById(id);
            if (!existingUser.isPresent())
            {
                throw new RuntimeException("User not found with ID: " + id);
            }

            User user = existingUser.get();

            if (user.isActive()==false)
            {
                throw new RuntimeException("User with ID " + id + " is already deactivated");
            }

            int result = userRepo.deactivateUser(id);
            if (result <= 0)
            {
                throw new RuntimeException("Failed to deactivate user with ID: " + id);

            }
            log.info("User with ID {} was successfully deactivated", id);
            return "SuccessFully deactivated the User";
    }



    @Override
    public String activateUser(Integer id) {

        Optional<User> existingUser = userRepo.findById(id);
        if (!existingUser.isPresent()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = existingUser.get();

        if (user.isActive()) {
            throw new RuntimeException("User with ID " + id + " is already active");
        }

        int result = userRepo.activateUser(id);
        if (result <= 0) {
            throw new RuntimeException("Failed to activate user with ID: " + id);
        }

        log.info("User with ID {} was successfully activated", id);
        return "Successfully activated the User";
    }



    @Override
    public String updateUserProfile(int userId, UserUpdateRequest dto) {

            Optional<User> optionalUser = userRepo.findById(userId);

            if (!optionalUser.isPresent()) {
                throw new RuntimeException("User not found with ID: " + userId);
            }

            User user = optionalUser.get();


            user.setName(dto.getName());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());

        userRepo.save(user);
            return "User profile updated successfully";


    }

    @Override
    public User updateUserByAdmin(UserAdminUpdateRequest user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update");
        }

        Optional<User> existingUserOpt = userRepo.findById(user.getId());
        if (!existingUserOpt.isPresent()) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " not found");
        }

        User existingUser = existingUserOpt.get();

        String encodedPassword;
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            encodedPassword = passwordEncoder.encode(user.getPassword());
        } else {
            encodedPassword = existingUser.getPassword();
        }

        int result = userRepo.updateUserFields(
                user.getId(),
                user.getName() != null ? user.getName() : existingUser.getName(),
                user.getEmail() != null ? user.getEmail() : existingUser.getEmail(),
                encodedPassword,
                user.getPhoneNumber() != null ? user.getPhoneNumber() : existingUser.getPhoneNumber(),
                user.getRole() != null ? user.getRole() : existingUser.getRole(),
                user.getStatus() != null ? user.getStatus() : existingUser.getStatus()
        );

        if (result <= 0) {
            throw new RuntimeException("Failed to update user with ID: " + user.getId());
        }

        return userRepo.findById(user.getId()).orElseThrow(() ->
                new RuntimeException("User update failed, record not found after update"));
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

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");
        }


        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
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


}








