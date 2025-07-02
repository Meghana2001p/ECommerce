package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Mapper.*;
import com.project.E_Commerce.Repository.ProductRepo;
import com.project.E_Commerce.Repository.UserFavouriteRepo;
import com.project.E_Commerce.Repository.UserRepo;
import com.project.E_Commerce.Repository.WishlistRepo;
import com.project.E_Commerce.Service.UserService;
import com.project.E_Commerce.dto.ChangePasswordRequest;
import com.project.E_Commerce.dto.FavouriteProductResponse;
import com.project.E_Commerce.dto.UserUpdateRequest;
import com.project.E_Commerce.dto.WishlistResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

////User,UserFavourite,Wishlist,UserEmailPreference,SearchHistory
@Slf4j
@Service
@RequiredArgsConstructor
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



    @Override
    public User createUser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            if (user.getConfirmPassword() != null &&
                    !user.getPassword().equals(user.getConfirmPassword())) {
                throw new IllegalArgumentException("Password and confirm password do not match");
            }


            // Check if user already exists
            if (userRepo.existsByEmail(user.getEmail())) {
                throw new UserAlreadyExists("User with email " + user.getEmail() + " already exists");
            }

            //if the user status is null
            if (user.getStatus() == null) {
                user.setStatus(User.Status.ACTIVE);
            }
            User createdUser = userRepo.save(user);
            if (createdUser.getUserId() == null && createdUser.getUserId() <= 0) {
                throw new DataCreationException("Failed to create user");

            }
            return createdUser;
        } catch (DataAccessException e)
//In Spring’s exception hierarchy,
// DataAccessException is the root unchecked exception for any problem
// that occurs while accessing the database (whether via JDBC, JPA, Hibernate, MyBatis, etc.).
        {
            logger.error("Database access error while creating user: {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal Service error");
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw e; // Rethrow so global handler can manage it
        }
    }

    @Override
    public User loginUser(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

        try {
            Optional<User> user = userRepo.findByEmailAndPassword(email, password);
            if (user == null) {
                throw new AuthenticationException("Invalid email or password");
            }
            return user.get();
        } catch (DataAccessException e)
//In Spring’s exception hierarchy,
// DataAccessException is the root unchecked exception for any problem
// that occurs while accessing the database (whether via JDBC, JPA, Hibernate, MyBatis, etc.).
        {
            logger.error("Database access error while creating user: {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal Service error");
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw e; // Rethrow so global handler can manage it
        }
    }

    @Override
    public String deactivateUser(Integer id) {
        try {
            Optional<User> existingUser = userRepo.findById(id);
            if (!existingUser.isPresent()) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            User user = existingUser.get();

            if (!user.isActive()) {
                throw new UserDeactivationException("User with ID " + id + " is already deactivated");
            }

            int result = userRepo.deactivateUser(id);
            if (result <= 0) {
                throw new DataUpdateException("Failed to deactivate user with ID: " + id);

            }
            log.info("User with ID {} was successfully deactivated", id);
            return "SuccessFully deactivated the User";
        } catch (DataAccessException e) {
            log.error("Error deactivating user with ID {}: {}", id, e.getMessage());
            throw new DataBaseException("Internal Server Error");
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw e; // Rethrow so global handler can manage it
        }


    }

    @Override
    public String updateUserProfile(int userId, UserUpdateRequest dto) {
        try {
            Optional<User> optionalUser = userRepo.findById(userId);

            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User not found with ID: " + userId);
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
        } catch (DataAccessException e) {
            log.error("Error updating the user", userId,e.getMessage());
            throw new DataBaseException("Internal Server Error");
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw e; // Rethrow so global handler can manage it
        }

    }

    @Override
    public User updateUserByAdmin(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update");
        }

        // Check if user exists
        try {
            Optional<User> existingUserOpt = userRepo.findById(user.getUserId());
            if (!existingUserOpt.isPresent()) {
                throw new UserNotFoundException("User with ID " + user.getUserId() + " not found");
            }


            // Run the JPQL update
            int result = userRepo.updateUserFields(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getRole(),
                    user.getStatus()
            );

            if (result <= 0) {
                throw new DataUpdateException("Failed to update user with ID: " + user.getUserId());
            }

            // Fetch updated user to return
            return userRepo.findById(user.getUserId()).orElseThrow(() ->
                    new UserNotFoundException("User not found after update")
            );
        }
        catch (DataAccessException e) {
            log.error("Error updating the user", user.getUserId(),e.getMessage());
            throw new DataBaseException("Internal Server Error");
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw e; // Rethrow so global handler can manage it
        }
    }


        @Override
    public void deleteUser(int userId) {
        try {

            Optional<User> user = userRepo.findById(userId);
            if (!user.isPresent()) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
            int deleteduser = userRepo.deleteByUserId(userId);
            if (deleteduser <= 0) {
                throw new IllegalArgumentException("Failed to delete user with ID " + userId);
            }
            log.info("User with ID {} deleted successfully", userId);

        } catch (DataAccessException e) {
            log.error("Error updating the user", userId, e.getMessage());
            throw new DataBaseException("Internal Server Error");
        } catch (Exception e) {
            log.error("Unexpected error while deleting user ID {}: {}", userId, e.getMessage(), e);
            throw e; // Rethrow so global handler can manage it
        }
    }

    @Override
    public String changePassword(int userId, ChangePasswordRequest request) {
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

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
        catch (DataAccessException e) {
            log.error("Error updating the password for user ID {}: {}", userId, e.getMessage(), e);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Unexpected error while updating user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String addToFavourites(int userId, int productId) {
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            Optional<UserFavourite> existing = userFavouriteRepo.findByUserIdAndProductId(userId, productId);

            if (existing.isPresent()) {
                UserFavourite favourite = existing.get();

                if (favourite.getIsLiked()) {
                    return "Product is already in favourites";
                } else {
                    // Toggle to true
                    userFavouriteRepo.updateFavouriteStatus(userId, productId, true);
                    return "Product re-added to favourites";
                }

            }

            // Insert new
            UserFavourite newFav = new UserFavourite(null, user, product, true, LocalDateTime.now());
            userFavouriteRepo.save(newFav);
            return "Product added to favourites";
        } catch (DataAccessException e) {
            log.error("Error adding the favourite  for user ID {}: {}", userId, e.getMessage(), e);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Unexpected error while adding the favourite  ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String removeFromFavourites(int userId, int productId) {
        try {
            Optional<UserFavourite> existing = userFavouriteRepo.findByUserIdAndProductId(userId, productId);

            if (existing.isEmpty() || !existing.get().getIsLiked()) {
                return "Product not found in favourites";
            }

            // Soft delete: set isLiked = false
            userFavouriteRepo.updateFavouriteStatus(userId, productId, false);
            return "Product removed from favourites";

        }
        catch (DataAccessException e) {
            log.error("Error removing the favourite  for user ID {}: {}", userId, e.getMessage(), e);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Unexpected error while removing the favourite  ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public List<FavouriteProductResponse> getUserFavouritesResponse(int userId) {

     try {

    User user = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    return userFavouriteRepo.findFavouriteProductDetailsByUserId(userId);

         }
catch (DataAccessException e) {
    log.info("User ID {} added product ID {} to favourites", userId);
    throw new DataBaseException("Internal Server Error");

} catch (Exception e) {
    log.error("Error fetching favourites for user ID {}: {}", userId, e.getMessage(), e);
    throw e;
}
    }










    //UserWishlist


    @Override
    public String addToWishlist(int userId, int productId) {
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            boolean exists = wishlistRepo.existsByUserIdAndProductId(userId, productId);
            if (exists) {
                return "Product already in wishlist";
            }
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlist.setProduct(product);
            wishlist.setProductName(product.getName());
            wishlist.setProductImageUrl(product.getImageAddress());
            wishlist.setAvailable(product.getIsAvailable());
            wishlist.setCreatedAt(LocalDateTime.now());

            wishlistRepo.save(wishlist);
            return "Product added to wishlist";
        }catch (DataAccessException e) {
            log.info("User ID {} added product ID {} to Wishlist", userId);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Error fetching Wishlist for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String removeFromWishlist(int userId, int productId) {
        try {
            Optional<Wishlist> existing = wishlistRepo.findByUserIdAndProductId(userId, productId);

            if (existing.isEmpty()) {
                return "Product not found in wishlist";
            }

            wishlistRepo.delete(existing.get());
            return "Product removed from wishlist";
        }catch (DataAccessException e) {
            log.info("User ID {} removed product ID {} to Wishlist", userId);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Error removing Wishlist for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<WishlistResponse> getWishlistByUser(int userId) {
        try {
            List<Wishlist> items = wishlistRepo.findByUserUserId(userId);
            return items.stream()
                    .map(w -> new WishlistResponse(
                            w.getProduct().getId(),
                            w.getProductName(),
                            w.getProductImageUrl(),
                            w.getAvailable(),
                            w.getCreatedAt()
                    ))
                    .toList();
        }
        catch (DataAccessException e) {
            log.info("couldnot get the data", userId);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Error fetching Wishlist for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String clearWishlist(int userId) {
        try {
            wishlistRepo.deleteByUserUserId(userId);
            return "Wishlist cleared for user ID: " + userId;
        }catch (DataAccessException e) {
            log.info("Could not clear from the wishlist", userId);
            throw new DataBaseException("Internal Server Error");

        } catch (Exception e) {
            log.error("Error fetching deleting for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }













}







//
//    @Override
//    public User updateUser(User user) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        if (user.getUserId() == null) {
//            throw new IllegalArgumentException("User ID must not be null for update");
//        }
//
//        try {
//            User existingUser = userMapper.getUserByID(user.getUserId());
//            if (existingUser == null) {
//                throw new UserNotFoundException("User not found with ID: " + user.getUserId());
//            }
//
//            int result = userMapper.updateUser(user);
//            if (result <= 0) {
//                throw new DataUpdateException("Failed to update User");
//            }
//
//            return user;
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update user: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public User getUserById(int userId) {
//        try {
//            User user = userMapper.getUserByID(userId);
//            if (user == null) {
//                throw new UserNotFoundException("User not found with ID: " + userId);
//            }
//            return user;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve user with ID " + userId);
//        }
//    }
//
//    @Override
//    public User userLogin(String email, String password) {
//        if (email == null || password == null) {
//            throw new IllegalArgumentException("Email and password cannot be null");
//        }
//        try {
//            User user = userMapper.userLogin(email, password);
//            if (user == null) {
//                throw new AuthenticationException("Invalid email or password");
//            }
//            return user;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to authenticate user: " + e.getMessage());
//        }
//    }
//
//    @Override
//    @Transactional
//    public void deactivateUser(int userId) {
//        try {
//            User user = userMapper.getUserByID(userId);
//            if (user == null) {
//                throw new UserNotFoundException("User not found with ID: " + userId);
//            }
//
//            // 2. Check if user is already deactivated
//            if (!user.isActive()) {
//                throw new UserDeactivationException("User with ID " + userId + " is already deactivated");
//            }
//
//
//            // 4. Deactivate the user account
//            int result = userMapper.deactivateUser(userId);
//
//            if (result <= 0) {
//                throw new DataUpdateException("Failed to deactivate user with ID: " + userId);
//            }
//
//            // 5. Log the deactivation
//            log.info("User with ID {} was successfully deactivated", userId);
//
//            // 6. Optionally send notification
//          //  notificationService.sendDeactivationNotification(user.getEmail());
//
//        } catch (DataAccessException e) {
//            log.error("Error deactivating user with ID {}: {}", userId, e.getMessage());
//            throw new DataDeletionException("Failed to deactivate user with ID " + userId + ": " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void deleteUser(int userId)
//    {
//        User user = userMapper.getUserByID(userId);
//        if (user == null) {
//            throw new UserNotFoundException("User not found with ID: " + userId);
//        }
//        try {
//            userMapper.deleteUser(userId);
//        }
//        catch (DataAccessException e)
//        {
//            throw new DataDeletionException("Failed to delete user with ID " + userId + ": " + e.getMessage());
//
//
//        }
//
//    }
//
//
//
//
//
//
//    //Email preferencs
//
//    @Transactional
//    public UserEmailPreferences createEmailPreference(UserEmailPreferences pref) {
//        try {
//            // Check if preference already exists
//            if (userEmailPreferencesMapper.getPreference(pref.getUserId(), pref.getEmailType().toString()) != null) {
//                throw new DuplicatePreferenceException(
//                        "Preference already exists for user " + pref.getUserId() +
//                                " and email type " + pref.getEmailType()
//                );
//            }
//
//            int affectedRows = userEmailPreferencesMapper.createPreference(pref);
//            if (affectedRows <= 0) {
//                throw new ServiceException("Failed to create email preference");
//            }
//            return pref;
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Database error while creating email preference");
//        }
//    }
//
//    @Transactional
//    public boolean updateEmailSubscription(int userId,
//                                           UserEmailPreferences.EmailType emailType,
//                                           boolean isSubscribed) {
//        try {
//            // Verify preference exists first
//            if (userEmailPreferencesMapper.getPreference(userId, emailType.toString()) == null) {
//                throw new PreferenceNotFoundException(
//                        "No preference found for user " + userId +
//                                " and email type " + emailType
//                );
//            }
//
//            int updated = userEmailPreferencesMapper.updateSubscriptionStatus(
//                    userId,
//                    emailType.toString(),
//                    isSubscribed
//            );
//
//            if (updated <= 0) {
//                throw new ServiceException("Failed to update email subscription");
//            }
//            return true;
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Database error while updating email subscription");
//        }
//    }
//
//    public List<UserEmailPreferences> getUserEmailPreferencesById(int userId) {
//        try {
//            List<UserEmailPreferences> preferences = userEmailPreferencesMapper.getPreferencesByUserId(userId);
//            if (preferences.isEmpty()) {
//                throw new PreferenceNotFoundException("No preferences found for user " + userId);
//            }
//            return preferences;
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Database error while fetching user preferences");
//        }
//    }
//
//    public UserEmailPreferences getPreference(int userId,
//                                              UserEmailPreferences.EmailType emailType) {
//        try {
//            UserEmailPreferences pref = userEmailPreferencesMapper.getPreference(
//                    userId,
//                    emailType.toString()
//            );
//            if (pref == null) {
//                throw new PreferenceNotFoundException(
//                        "No preference found for user " + userId +
//                                " and email type " + emailType
//                );
//            }
//            return pref;
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Database error while fetching preference");
//        }
//    }
//
//    @Transactional
//    public boolean deletePreference(int userId,
//                                    UserEmailPreferences.EmailType emailType) {
//        try {
//            // Verify exists before deletion
//            if (userEmailPreferencesMapper.getPreference(userId, emailType.toString()) == null) {
//                throw new PreferenceNotFoundException(
//                        "Cannot delete - no preference found for user " + userId +
//                                " and email type " + emailType
//                );
//            }
//
//            int deleted = userEmailPreferencesMapper.deletePreference(userId, emailType.toString());
//            if (deleted == 0) {
//                throw new ServiceException("Failed to delete email preference");
//            }
//            return true;
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Database error while deleting preference");
//        }
//    }
//
//    public boolean isSubscribedTo(int userId, String emailType) {
//        try {
//            UserEmailPreferences pref = userEmailPreferencesMapper.getPreference(userId, emailType);
//            if (pref == null) {
//                throw new PreferenceNotFoundException(
//                        "No preference found for user " + userId +
//                                " and email type " + emailType
//                );
//            }
//            return pref.getIsSubscribed();
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Database error while checking subscription status");
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public List<UserFavourite> getUserFavourites(int userId) {
//        try {
//            List<UserFavourite> favourites = userFavouriteMapper.getFavouritesByUserId(userId);
//            if (favourites == null || favourites.isEmpty()) {
//                log.info("No favourites found for user ID: {}", userId);
//            }
//            return favourites;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve favourites for user ID " + userId);
//        }
//    }
//
//    @Override
//    public UserFavourite getFavouriteByUserAndProduct(int userId, int productId) {
//        try {
//            UserFavourite favourite = userFavouriteMapper.getByUserAndProduct(userId, productId);
//            if (favourite == null) {
//                log.info("No favourite found for user ID {} and product ID {}", userId, productId);
//            }
//            return favourite;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve favourite for user ID " + userId + " and product ID " + productId);
//        }
//    }
//
//    @Override
//    public String addToFavourites(UserFavourite userFavourite) {
//        if (userFavourite == null) {
//            throw new IllegalArgumentException("User favourite cannot be null");
//        }
//        try {
//            // Check if already exists
//            UserFavourite existing = userFavouriteMapper.getByUserAndProduct(
//                    userFavourite.getUserId(),
//                    userFavourite.getProductId()
//            );
//            if (existing != null) {
//                throw new DuplicateResourceException("This product is already in user's favourites");
//            }
//
//            int res = userFavouriteMapper.insertUserFavourite(userFavourite);
//            if (res <= 0) {
//                throw new DataCreationException("Cannot add the User favourite");
//            }
//            return "User Favourite added successfully";
//        } catch (DuplicateKeyException e) {
//            throw new DuplicateResourceException("This product is already in user's favourites");
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to add to favourites: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String updateFavouriteStatus(int userId, int productId, boolean isLiked) {
//        try {
//            int res = userFavouriteMapper.updateFavouriteStatus(userId, productId, isLiked);
//            if (res <= 0) {
//                throw new DataUpdateException("The user favourites cannot be updated");
//            }
//            return "User favourites Updated";
//        } catch (DataAccessException e) {
//            throw new DataUpdateException("Failed to update favourite status: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String removeFromFavourites(int userId, int productId) {
//        try {
//            int res = userFavouriteMapper.deleteByUserAndProduct(userId, productId);
//            if (res <= 0) {
//                throw new NotFoundException("The User favourite cannot be removed or does not exist");
//            }
//            return "User favourite removed successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to remove from favourites: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Wishlist> getWishlistByUserId(int userId) {
//        try {
//            List<Wishlist> wishlist = wishlistMapper.getWishlistByUserId(userId);
//            if (wishlist == null || wishlist.isEmpty()) {
//                log.info("No wishlist items found for user ID: {}", userId);
//            }
//            return wishlist;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve wishlist for user ID " + userId);
//        }
//    }
//
//    @Override
//    public String addToWishlist(Wishlist wishlist) {
//        if (wishlist == null) {
//            throw new IllegalArgumentException("Wishlist item cannot be null");
//        }
//        try {
//            // Check if already exists
//            Wishlist existing = wishlistMapper.getByUserAndProduct(
//                    wishlist.getUserId(),
//                    wishlist.getProductId()
//            );
//            if (existing != null) {
//                throw new DuplicateResourceException("This product is already in user's wishlist");
//            }
//
//            int res = wishlistMapper.insertWishlistItem(wishlist);
//            if (res <= 0) {
//                throw new DataCreationException("Wishlist could not be updated");
//            }
//            return "Wishlist updated successfully";
//        } catch (DuplicateKeyException e) {
//            throw new DuplicateResourceException("This product is already in user's wishlist");
//        } catch (DataAccessException e) {
//            throw new DataCreationException("Failed to add to wishlist: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String removeWishlistItemById(int id) {
//        try {
//            int res = wishlistMapper.deleteWishlistItem(id);
//            if (res <= 0) {
//                throw new NotFoundException("Could not remove the wishlist item or it does not exist");
//            }
//            return "Wishlist Item removed successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to remove wishlist item: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String removeWishlistItemByUserAndProduct(int userId, int productId) {
//        try {
//            int res = wishlistMapper.deleteByUserAndProduct(userId, productId);
//            if (res <= 0) {
//                throw new NotFoundException("Could not remove the wishlist item or it does not exist");
//            }
//            return "Wishlist Item removed successfully";
//        } catch (DataAccessException e) {
//            throw new DataDeletionException("Failed to remove wishlist item: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Wishlist getWishlistItem(int userId, int productId) {
//        try {
//            Wishlist wishlistItem = wishlistMapper.getByUserAndProduct(userId, productId);
//            if (wishlistItem == null) {
//                log.info("No wishlist item found for user ID {} and product ID {}", userId, productId);
//            }
//            return wishlistItem;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve wishlist item: " + e.getMessage());
//        }
//    }
//
//
//
//
//
//
//
//    //Search history
//
//    @Override
//    public void addSearchHistory(SearchHistory searchHistory) {
//        if (searchHistory == null) {
//            throw new IllegalArgumentException("Search history cannot be null");
//        }
//        try {
//            if (searchHistory.getSearchedAt() == null) {
//                searchHistory.setSearchedAt(LocalDateTime.now());
//            }
//            int res = searchHistoryMapper.createSearchHistory(searchHistory);
//            if (res <= 0) {
//                log.warn("Search history insertion failed for userId: {}", searchHistory.getUserId());
//                throw new DataCreationException("Failed to add search history");
//            }
//        } catch (DataAccessException e) {
//            log.error("Failed to add search history: {}", e.getMessage());
//            throw new DataCreationException("Failed to add search history: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<SearchHistory> getAllSearchHistory() {
//        try {
//            return searchHistoryMapper.getAllSearchHistory();
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve all search history");
//        }
//    }
//
//    @Override
//    public List<SearchHistory> getSearchHistoryByUserId(int userId) {
//        try {
//            List<SearchHistory> history = searchHistoryMapper.getSearchHistoryByUserId(userId);
//            if (history == null || history.isEmpty()) {
//                log.info("No search history found for user ID: {}", userId);
//            }
//            return history;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve search history for user ID " + userId);
//        }
//    }
//
//    @Override
//    public List<SearchHistory> getSearchHistoryBySessionId(String sessionId) {
//        if (sessionId == null || sessionId.isEmpty()) {
//            throw new IllegalArgumentException("Session ID cannot be null or empty");
//        }
//        try {
//            List<SearchHistory> history = searchHistoryMapper.getSearchHistoryBySessionId(sessionId);
//            if (history == null || history.isEmpty()) {
//                log.info("No search history found for session ID: {}", sessionId);
//            }
//            return history;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve search history for session ID " + sessionId);
//        }
//    }
//
//    @Override
//    public List<SearchHistory> getRecentSearchesForUser(int userId, int page) {
//        if (page < 1) {
//            throw new IllegalArgumentException("Page number must be greater than 0");
//        }
//        try {
//            int size = 10;
//            int offset = (page - 1) * size;
//            List<SearchHistory> recentSearches = searchHistoryMapper.getRecentSearchesForUser(userId, size, offset);
//            if (recentSearches == null || recentSearches.isEmpty()) {
//                log.info("No recent searches found for user ID: {}", userId);
//            }
//            return recentSearches;
//        } catch (DataAccessException e) {
//            throw new DataRetrievalException("Failed to retrieve recent searches for user ID " + userId);
//        }
//    }
//
//    @Override
//    public void clearSearchHistoryForUser(int userId) {
//        try {
//            // Validate user ID first
//            if (userId <= 0) {
//                throw new IllegalArgumentException("User ID must be a positive number");
//            }
//
//            int affectedRows = searchHistoryMapper.deleteSearchHistoryById(userId);
//
//            // Optional: Check if any rows were actually deleted
//            if (affectedRows == 0) {
//
//                throw new NoContentException("No search history found for user: " + userId);
//            }
//
//
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Failed to clear search history for user");
//        }
//    }
//
//    @Override
//    public void clearSearchHistoryForSession(String sessionId) {
//        try {
//            if (sessionId == null || sessionId.isEmpty()) {
//                throw new IllegalArgumentException("Session ID cannot be null or empty");
//            }
//
//            int affectedRows = searchHistoryMapper.deleteSearchHistoryBySessionId(sessionId);
//
//            if (affectedRows == 0) {
//                throw new NoContentException("No search history found for session: " + sessionId);
//            }
//        } catch (DataAccessException ex) {
//            throw new ServiceException("Failed to clear search history for session");
//        }
//    }

