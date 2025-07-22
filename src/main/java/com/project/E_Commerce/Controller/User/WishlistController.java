package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Service.UserWishlistService;
import com.project.E_Commerce.dto.WishlistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private UserWishlistService userWishlistService;

    //add to the wishlist
    @PostMapping("/")
    public ResponseEntity<?> addToWishlist(@RequestBody  WishlistRequest request)
    {
    String message=  userWishlistService.addToWishlist(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(message);
    }
  //remove from the wishlist
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFromWishlist(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {

        userWishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok("Product removed from wishlist");
    }

    //get  the product Details in the wishlist



}
