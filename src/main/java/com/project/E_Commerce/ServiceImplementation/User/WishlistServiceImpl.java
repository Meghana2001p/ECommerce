package com.project.E_Commerce.ServiceImplementation.User;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.ProductImage;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Entity.User.Wishlist;
import com.project.E_Commerce.Exception1.ResourceAlreadyExistsException;
import com.project.E_Commerce.Repository.Product.ProductDiscountRepo;
import com.project.E_Commerce.Repository.Product.ProductImageRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.Product.ReviewRepo;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Repository.User.WishlistRepo;
import com.project.E_Commerce.Service.User.UserWishlistService;
import com.project.E_Commerce.dto.User.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements UserWishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductDiscountRepo productDiscountRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private ProductImageRepo productImageRepo;

    @Override
    public String addToWishlist(int userId, int productId)
    {

        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Optional<Wishlist> existingWishlist = wishlistRepo.findByUserIdAndProductId(userId, productId);
        if (existingWishlist.isPresent()) {
         throw new ResourceAlreadyExistsException("Product already exists in the wishlist");
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepo.save(wishlist);

        return "Product added to the wishlist";
    }

    @Override
    public String removeFromWishlist(int userId, int productId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        if (!productRepo.existsById(productId)) {
            throw new IllegalArgumentException("Product not found");
        }
        Optional<Wishlist> existingWishlist = wishlistRepo.findByUserIdAndProductId(userId, productId);
        if (existingWishlist.isPresent()) {

         int res= wishlistRepo.deleteByUserIdAndProductId(userId, productId);
         if(res>0)
         {
            return "Product removed successfully from the  wishlist";}
        }
            return "Product doesnot exist in the wishlist";

    }

    @Override
    public List<WishlistResponse> getWishlistProductsByUserId(int userId) {
        // Step 1: Get all Wishlist entries for the user
        List<Wishlist> wishlistItems = wishlistRepo.findByUserId(userId);

        // Step 2: Extract products from the wishlist
        List<Product> products = wishlistItems.stream()
                .map(Wishlist::getProduct)
                .toList();

        // Step 3: Map to WishlistResponse DTOs
        return products.stream().map(p -> {
            BigDecimal discountPercent = getActiveDiscountPercent(p.getId());
            BigDecimal discountedPrice = applyDiscount(p.getPrice(), discountPercent);
            Double avgRating = reviewRepo.findAverageRatingByProductId(p.getId());
            Integer reviewCount = reviewRepo.countByProductId(p.getId());

            List<String> imageUrls = productImageRepo.findByProductId(p.getId())
                    .stream()
                    .map(ProductImage::getImageUrl)
                    .toList();

            return new WishlistResponse(
                    p.getId(),
                    p.getName(),
                    p.getBrand().getBrandName(),
                    p.getDescription(),
                    p.getImageAddress(), // Thumbnail
                    p.getPrice(),
                    discountedPrice,
                    discountPercent != null ? discountPercent.intValue() : 0,
                    avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                    reviewCount,
                    p.getIsAvailable(),
                    null, // Optional label
                    imageUrls
            );
        }).toList();
    }


    private BigDecimal getActiveDiscountPercent(Integer productId) {
        Optional<BigDecimal> discounts = productDiscountRepo.findDiscountPercentByProductId(productId);
        if (discounts.isEmpty()) return null;
        return  discounts.get();
    }

    private BigDecimal applyDiscount(BigDecimal price, BigDecimal percent) {
        if (percent == null) return price;
        return price.subtract(price.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }








}
