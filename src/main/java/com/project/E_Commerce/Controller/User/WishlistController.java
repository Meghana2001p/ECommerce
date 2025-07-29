package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Service.User.UserWishlistService;
import com.project.E_Commerce.dto.User.WishlistRequest;
import com.project.E_Commerce.dto.User.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private UserWishlistService userWishlistService;

    //add to the wishlist
    @PostMapping("/")
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistRequest request)
    {
    String message=  userWishlistService.addToWishlist(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(message);
    }
  //remove from the wishlist
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWishlist(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {

        String message=     userWishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(message);
    }

    //get  the product Details in the wishlist

    @GetMapping("/{userId}")
    public ResponseEntity<List<?>> getWishlistByUserId(@PathVariable int userId) {
        List<WishlistResponse> wishlistProducts = userWishlistService.getWishlistProductsByUserId(userId);
        return ResponseEntity.ok(wishlistProducts);
    }

}
