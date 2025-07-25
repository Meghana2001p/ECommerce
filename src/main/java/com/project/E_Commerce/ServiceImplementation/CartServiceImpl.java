package com.project.E_Commerce.ServiceImplementation;



import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public  class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);


    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private AppliedCouponRepo appliedCouponRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ProductDiscountRepo productDiscountRepo;


    @Override
    public void addProductToCart(CartRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Invalid data");
        }

        Cart cart = cartRepo.findByUserId(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));


        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (productRepo.isAvaliable(product.getId())) {
            CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId())
                    .map(existingItem -> {
                        existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                        return existingItem;
                    })
                    .orElseGet(() -> {
                        CartItem newItem = new CartItem();
                        newItem.setCart(cart);
                        newItem.setProduct(product);
                        newItem.setQuantity(request.getQuantity());
                        newItem.setPrice(product.getPrice());
                        return newItem;
                    });

            cartItemRepo.save(item);
        }
    }

    @Override
    public void removeProductFromCart(Integer userId, Integer productId) {
        if (userId <= 0 || productId <= 0) {
            throw new IllegalArgumentException("Invalid data");
        }
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        cartItemRepo.delete(item);
    }




    public CartSummaryResponse calculateCartSummary(List<CartItemProjection> cartItems) {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalCouponDiscount = BigDecimal.ZERO;
        int totalQuantity = 0;
        Set<Integer> uniqueProducts = new HashSet<>();

        for (CartItemProjection item : cartItems) {
            BigDecimal price = item.getCartItemPrice() != null ? item.getCartItemPrice() : item.getOriginalPrice();

            if (item.getDiscountPercent() != null) {
                BigDecimal discountAmount = price.multiply(item.getDiscountPercent().divide(BigDecimal.valueOf(100)));
                price = price.subtract(discountAmount);
            }

            BigDecimal totalPriceForThisItem = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            subTotal = subTotal.add(totalPriceForThisItem);
            totalQuantity += item.getQuantity();
            uniqueProducts.add(item.getProductId());

            // Coupon discount (assuming it's flat across all items)
            if (item.getCouponDiscountAmount() != null) {
                totalCouponDiscount = item.getCouponDiscountAmount();  // One-time deduction
            }
        }

        BigDecimal totalGST = subTotal.multiply(BigDecimal.valueOf(0.18));  // 18% GST
        BigDecimal grandTotal = subTotal.add(totalGST).subtract(totalCouponDiscount);

        CartSummaryResponse response = new CartSummaryResponse();
        response.setSubTotal(subTotal);
        response.setTotalGST(totalGST);
        response.setCouponDiscount(totalCouponDiscount);
        response.setGrandTotal(grandTotal);
        response.setTotalItems(uniqueProducts.size());
        response.setTotalQuantity(totalQuantity);

        return response;
    }


    public List<CartItemResponse> buildCartItemResponses(List<CartItemProjection> cartItems) {
        List<CartItemResponse> responses = new ArrayList<>();

        for (CartItemProjection item : cartItems) {
            BigDecimal price = item.getCartItemPrice() != null ? item.getCartItemPrice() : item.getOriginalPrice();

            BigDecimal discountPercent = item.getDiscountPercent() != null ? item.getDiscountPercent() : BigDecimal.ZERO;
            BigDecimal discountAmount = price.multiply(discountPercent.divide(BigDecimal.valueOf(100)));

            BigDecimal priceAfterDiscount = price.subtract(discountAmount);
            BigDecimal totalPriceForThisItem = priceAfterDiscount.multiply(BigDecimal.valueOf(item.getQuantity()));

            CartItemResponse response = new CartItemResponse(
                    item.getProductId(),
                    item.getName(),
                    item.getImageUrl(),
                    price,
                    discountPercent,
                    discountAmount,
                    priceAfterDiscount,
                    item.getQuantity(),
                    totalPriceForThisItem,
                    item.getProductRating()
            );

            responses.add(response);
        }

        return responses;
    }


    public List<CouponResponseCart> buildCouponResponses(List<CartItemProjection> coupons) {
        List<CouponResponseCart> responses = new ArrayList<>();

        for (CartItemProjection coupon : coupons) {
            CouponResponseCart response = new CouponResponseCart(
                    coupon.getCouponName(),
                    coupon.getCouponDiscountAmount());
            responses.add(response);
        }

        return responses;
    }






    @Override
    public CartResponse viewCart(Integer userId) {
        // 1. Fetch cart items using projection
        List<CartItemProjection> cartItems = cartItemRepo.getAllCartItems(userId);

        // 2. Build cart item responses
        List<CartItemResponse> cartItemResponses = buildCartItemResponses(cartItems);

        // 3. Build cart summary
        CartSummaryResponse summary = calculateCartSummary(cartItems);

        // 4. Extract coupon (if any)
        CouponResponseCart coupon = null;
        for (CartItemProjection item : cartItems) {
            if (item.getCouponName() != null && item.getCouponDiscountAmount() != null) {
                coupon = new CouponResponseCart(item.getCouponName(), item.getCouponDiscountAmount());
                break; // Only one coupon expected
            }
        }

        // 5. Build and return final response
        CartResponse cartResponse = new CartResponse();
        cartResponse.setUserId(userId);
        cartResponse.setCartItems(cartItemResponses);
        cartResponse.setSummary(summary);
        cartResponse.setCoupon(coupon);

        return cartResponse;
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








































    @Override
    @Transactional
    public Coupon createCoupon(Coupon coupon) {

        if (coupon == null || coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code cannot be null or empty");
        }
        // Trim the code
        coupon.setCode(coupon.getCode().trim());
        // Check for duplicate
        if (couponRepo.findByCode(coupon.getCode()).isPresent()) {
            throw new CouponAlreadyExistsException("Coupon already exists");
        }
        // Validate usage limit
        if (coupon.getUsageLimit() == null || coupon.getUsageLimit() < 1) {
            throw new IllegalArgumentException("Usage limit must be at least 1");
        }
        // Set active status
        coupon.setIsActive(true);
        return couponRepo.save(coupon);
    }



    @Override
    public List<CouponResponse> getAllAvailableCoupons() {
        return couponRepo.findAll().stream().map(coupon -> {
            CouponResponse response = new CouponResponse();
            response.setCode(coupon.getCode());
            response.setDiscountAmount(coupon.getDiscountAmount());
            response.setExpiryDate(coupon.getExpiryDate());
            response.setUsageLimit(coupon.getUsageLimit());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateCouponById(Integer couponId, CouponRequest dto)
    {
        if(couponId<=0)
        {
            throw  new IllegalArgumentException("Coupon id is invalid");
        }
        Coupon existingCoupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        // Check if the new code is already taken by another coupon
        Optional<Coupon> existingWithSameCode = couponRepo.findByCode(dto.getCode());
        if (existingWithSameCode.isPresent() && !existingWithSameCode.get().getId().equals(couponId)) {
            throw new IllegalArgumentException("Coupon code already exists");
        }

        existingCoupon.setCode(dto.getCode());
        existingCoupon.setDiscountAmount(dto.getDiscountAmount());
        existingCoupon.setExpiryDate(dto.getExpiryDate());
        existingCoupon.setUsageLimit(dto.getUsageLimit());

        couponRepo.save(existingCoupon);
    }

    @Override
    public void deleteCouponById(Integer couponId) {
        if(couponId<=0)
        {
            throw  new IllegalArgumentException("Coupon id is invalid");
        }
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with ID: " + couponId));

        couponRepo.delete(coupon);
    }




    @Override
    public CartAmountSummaryDto calculateCartSummary(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }

        try {
            Cart cart = cartRepo.findByUserId(userId)
                    .orElseThrow(() -> new CartNotFoundException("Cart not found"));

            List<CartItem> items = cartItemRepo.findByCartId(cart.getId());
            if (items.isEmpty()) {
                throw new CartNotFoundException("Cart has no items");
            }

            BigDecimal subtotal = BigDecimal.ZERO;
            for (CartItem item : items) {
                subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            BigDecimal discount = BigDecimal.ZERO;
            String couponCode = null;
            AppliedCoupon appliedCoupon = appliedCouponRepo.findByCartId(cart.getId());

            if (appliedCoupon != null) {
                Coupon coupon = couponRepo.findById(appliedCoupon.getCoupon().getId()).orElse(null);
                if (coupon != null) {
                    couponCode = coupon.getCode();

//                    if (coupon.getDiscountPercent() != null) {
//                        discount = subtotal.multiply(coupon.getDiscountPercent())
//                                .divide(BigDecimal.valueOf(100));
//                    } else if (coupon.getDiscountAmount() != null) {
//                        discount = coupon.getDiscountAmount();
//                    }
                }
            }

            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.10));
            BigDecimal shipping = BigDecimal.valueOf(50);
            BigDecimal total = subtotal.subtract(discount).add(tax).add(shipping);

            return new CartAmountSummaryDto(subtotal, discount, tax, shipping, total, couponCode);

        } catch (DataAccessException e) {
            logger.error("DB error while calculating cart summary for user {}: {}", userId, e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error while calculating cart summary for user {}", userId, e);
            throw e;
        }
    }

}