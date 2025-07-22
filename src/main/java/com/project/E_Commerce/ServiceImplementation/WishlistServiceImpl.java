package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.User;
import com.project.E_Commerce.Entity.Wishlist;
import com.project.E_Commerce.Repository.ProductRepo;
import com.project.E_Commerce.Repository.UserRepo;
import com.project.E_Commerce.Repository.WishlistRepo;
import com.project.E_Commerce.Service.UserWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements UserWishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;


    @Override
    public String addToWishlist(int userId, int productId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepo.save(wishlist);

        return "Product added to the wishlist";
    }

    @Override
    public String removeFromWishlist(int userId, int productId)
    {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        if (!productRepo.existsById(productId))
        {
            throw new IllegalArgumentException("Product not found");
        }

        wishlistRepo.deleteByUserIdAndProductId(userId, productId);
        return "Product removed successfully from the  wishlist";

    }



}
