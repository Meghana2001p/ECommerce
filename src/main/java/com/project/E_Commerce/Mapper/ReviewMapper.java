package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.Review;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.dto.Product.ReviewRequestDto;
import com.project.E_Commerce.dto.Product.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(source = "productId", target = "product", qualifiedByName = "mapProduct")
    Review toEntity(ReviewRequestDto dto);

    @Named("mapUser")
    default User mapUser(Integer userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("mapProduct")
    default Product mapProduct(Integer productId) {
        if (productId == null) return null;
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    ReviewResponse toResponse(Review review);
}
