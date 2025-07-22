package com.project.E_Commerce.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDetail
{
        private Integer productId;
        private String name;
        private String description;
        private String sku;
        private BigDecimal price;
        private Boolean isAvailable;
        private String brandName;
        private List<String> imageUrls;
        private List<ProductAttributeResponse> attributes;
        private DiscountResponse activeDiscount;
        private List<ReviewResponse> reviews;
        private Double averageRating;
        private List<RelatedProductResponse> relatedProducts;
        private List<CouponResponse1> availableCoupons;
        private DeliveryInfoResponse deliveryInfo;


        //in cart and the wishlist
        private Boolean inWishlist;
        private Boolean inCart;


        public ProductDetail(Integer productId, String name, String description, String sku, BigDecimal price,
                             Boolean isAvailable, String brandName, List<String> imageUrls,
                             List<ProductAttributeResponse> attributes,
                             DiscountResponse activeDiscount, List<ReviewResponse> reviews,
                             Double averageRating, List<RelatedProductResponse> relatedProducts,
                             List<CouponResponse1> availableCoupons, DeliveryInfoResponse deliveryInfo) {
                this.productId = productId;
                this.name = name;
                this.description = description;
                this.sku = sku;
                this.price = price;
                this.isAvailable = isAvailable;
                this.brandName = brandName;
                this.imageUrls = imageUrls;
                this.attributes = attributes;
                this.activeDiscount = activeDiscount;
                this.reviews = reviews;
                this.averageRating = averageRating;
                this.relatedProducts = relatedProducts;
                this.availableCoupons = availableCoupons;
                this.deliveryInfo = deliveryInfo;
        }


        public ProductDetail(
                Integer productId,
                String name,
                String description,
                String sku,
                BigDecimal price,
                Boolean isAvailable,
                String brandName,
                List<String> imageUrls,
                List<ProductAttributeResponse> attributes,
                DiscountResponse activeDiscount,
                List<ReviewResponse> reviews,
                Double averageRating,
                List<RelatedProductResponse> relatedProducts,
                List<CouponResponse1> availableCoupons,
                DeliveryInfoResponse deliveryInfo,
                Boolean inWishlist,
                Boolean inCart) {

                this.productId = productId;
                this.name = name;
                this.description = description;
                this.sku = sku;
                this.price = price;
                this.isAvailable = isAvailable;
                this.brandName = brandName;
                this.imageUrls = imageUrls;
                this.attributes = attributes;
                this.activeDiscount = activeDiscount;
                this.reviews = reviews;
                this.averageRating = averageRating;
                this.relatedProducts = relatedProducts;
                this.availableCoupons = availableCoupons;
                this.deliveryInfo = deliveryInfo;
                this.inWishlist = inWishlist;
                this.inCart = inCart;
        }
}
