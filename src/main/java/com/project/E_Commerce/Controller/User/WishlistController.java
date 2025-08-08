package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Service.User.UserWishlistService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import com.project.E_Commerce.dto.User.WishlistRequest;
import com.project.E_Commerce.dto.User.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private UserWishlistService userWishlistService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/")
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistRequest request)
    {
        if(getCurrentUserId()!=request.getUserId())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonList("Access denied: You can only access your own data"));
        }
        String message=  userWishlistService.addToWishlist(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(message);
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Integer userId,
            @PathVariable Integer productId)
    {
        if(getCurrentUserId()!=userId)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonList("Access denied: You can only access your own data"));
        }
        String message=     userWishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(message);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<?>> getWishlistByUserId(@PathVariable int userId)
    {
        if(getCurrentUserId()!=userId)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonList("Access denied: You can only access your own data"));
        }
        List<WishlistResponse> wishlistProducts = userWishlistService.getWishlistProductsByUserId(userId);
        return ResponseEntity.ok(wishlistProducts);
    }

    public int getCurrentUserId()
    {
        //get the authentication object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl)
        {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            return userDetails.getId();

        }
        throw new RuntimeException("Unauthorized access");
    }


}
