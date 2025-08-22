package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.Cart.Coupon;
import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Product.*;
import com.project.E_Commerce.dto.Product.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "attribute", source = "attribute")
    ProductAttributeValue toEntity(ProductAttributeAssignmentRequest.AttributeValuePair dto, Product product, ProductAttribute attribute);

    ProductDetail toProductDetail(ProductWithBrandProjection projection);

    default List<String> mapImagesToUrls(List<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(ProductImage::getImageUrl)
                .toList();
    }

    @Mapping(target = "attributeName", source = "attribute.name")
    @Mapping(target = "value", source = "value")
    ProductAttributeResponse mapAttribute(ProductAttributeValue pav);

    List<ProductAttributeResponse> mapAttributes(List<ProductAttributeValue> pavs);

    InventoryResponse toInventoryResponse(Inventory inventory);


    DiscountResponse toActiveDiscountResponse(Discount discount);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    ReviewResponse toReviewResponse(Review review);

    List<ReviewResponse> toReviewResponseList(List<Review> reviews);


    List<RelatedProductResponse> toRelatedProductResponseList(List<RelatedProduct> related);


    List<CouponResponse1> toCouponResponseList(List<Coupon> coupons);
}



/* * Converts from [InputDTO] and additional entities ([Entity1], [Entity2], etc.)
 * into [TargetEntity].

@Mapper(componentModel = "spring")
public interface YourEntityMapper {

    @Mapping(target = "id", ignore = true) // Ignore ID if it's auto-generated
    @Mapping(target = "fieldX", source = "entity1") // Map custom object
    @Mapping(target = "fieldY", source = "entity2") // Map another object
    TargetEntity toEntity(
            InputDTO dto,
            Entity1 entity1,
            Entity2 entity2
    );
}*/
