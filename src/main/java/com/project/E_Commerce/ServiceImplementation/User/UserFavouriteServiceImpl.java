package com.project.E_Commerce.ServiceImplementation.User;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.ProductImage;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Entity.User.UserFavourite;
import com.project.E_Commerce.Exception.ResourceAlreadyExistsException;
import com.project.E_Commerce.Repository.Product.ProductDiscountRepo;
import com.project.E_Commerce.Repository.Product.ProductImageRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.Product.ReviewRepo;
import com.project.E_Commerce.Repository.User.UserFavouriteRepo;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Service.User.UserFavouriteService;
import com.project.E_Commerce.dto.User.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class UserFavouriteServiceImpl implements UserFavouriteService {

    @Autowired
    private UserFavouriteRepo favouriteRepo;

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
    public String addToFavourites(int userId, int productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<UserFavourite> existing = favouriteRepo.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            throw new ResourceAlreadyExistsException("Product already marked as favourite");
        }

        UserFavourite favourite = new UserFavourite();
        favourite.setUser(user);
        favourite.setProduct(product);
        favouriteRepo.save(favourite);
        return "Product added to favourites";
    }

    @Override
    public String removeFromFavourites(int userId, int productId) {
        if (!userRepo.existsById(userId)) throw new IllegalArgumentException("User not found");
        if (!productRepo.existsById(productId)) throw new IllegalArgumentException("Product not found");

        Optional<UserFavourite> existing = favouriteRepo.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            int deleted = favouriteRepo.deleteByUserIdAndProductId(userId, productId);
            if (deleted > 0) return "Product removed from favourites";
        }
        return "Product was not in favourites";
    }

    @Override
    public List<WishlistResponse> getFavouritedProductsByUserId(int userId) {
        List<UserFavourite> favItems = favouriteRepo.findByUserId(userId);
        List<Product> products = favItems.stream()
                .map(UserFavourite::getProduct)
                .toList();

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
                    p.getImageAddress(),
                    p.getPrice(),
                    discountedPrice,
                    discountPercent != null ? discountPercent.intValue() : 0,
                    avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0,
                    reviewCount,
                    p.getIsAvailable(),
                    null,
                    imageUrls
            );
        }).toList();
    }

    private BigDecimal getActiveDiscountPercent(Integer productId) {
        return productDiscountRepo.findDiscountPercentByProductId(productId).orElse(null);
    }

    private BigDecimal applyDiscount(BigDecimal price, BigDecimal percent) {
        if (percent == null) return price;
        return price.subtract(price.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }
}

