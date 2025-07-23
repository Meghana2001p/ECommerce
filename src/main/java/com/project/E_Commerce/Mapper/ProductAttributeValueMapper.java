package com.project.E_Commerce.Mapper;
import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collections;
import java.util.List;


//
// * Converts from [InputDTO] and additional entities ([Entity1], [Entity2], etc.)
// * into [TargetEntity].
// */
//@Mapper(componentModel = "spring")
//public interface YourEntityMapper {
//
//    @Mapping(target = "id", ignore = true) // Ignore ID if it's auto-generated
//    @Mapping(target = "fieldX", source = "entity1") // Map custom object
//    @Mapping(target = "fieldY", source = "entity2") // Map another object
//    TargetEntity toEntity(
//            InputDTO dto,
//            Entity1 entity1,
//            Entity2 entity2
//    );
//}

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {

  // Converts DTO input + entity objects into the final entity
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "product", source = "product")
  @Mapping(target = "attribute", source = "attribute")
  ProductAttributeValue toEntity(ProductAttributeAssignmentRequest.AttributeValuePair dto, Product product, ProductAttribute attribute);



  //ProductDetails

  ProductDetail toProductDetail(ProductWithBrandProjection projection);


  //Images
  default List<String> mapImagesToUrls(List<ProductImage> images) {
    if (images == null) return Collections.emptyList();
    return images.stream()
            .map(ProductImage::getImageUrl)
            .toList();
  }



  // ✅ Attribute value mapping
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
