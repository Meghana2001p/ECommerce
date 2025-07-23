package com.project.E_Commerce.Controller.User;

import com.project.E_Commerce.Service.*;
import com.project.E_Commerce.dto.FavouriteRequest;
import com.project.E_Commerce.dto.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favourites")
public class UserFavouriteController {

    @Autowired
    private UserFavouriteService userFavouriteService;

    // Add to favourites
    @PostMapping("/")
    public ResponseEntity<String> addToFavourites(@RequestBody FavouriteRequest request) {
        String message = userFavouriteService.addToFavourites(request.getUserId(), request.getProductId());
        return ResponseEntity.ok(message);
    }

    // Remove from favourites
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFromFavourites(@PathVariable Integer userId, @PathVariable Integer productId) {
        String message = userFavouriteService.removeFromFavourites(userId, productId);
        return ResponseEntity.ok(message);
    }

    // Get favourited products for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<?>> getFavouritedProducts(@PathVariable int userId) {
        List<WishlistResponse> products = userFavouriteService.getFavouritedProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }
}
