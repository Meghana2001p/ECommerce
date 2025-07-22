package com.project.E_Commerce.ServiceImplementation;



import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.dto.CartAmountSummaryDto;
import com.project.E_Commerce.dto.CartItemDto;
import com.project.E_Commerce.dto.CouponRequest;
import com.project.E_Commerce.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Cart createCart(Cart cart)
    {
        try {
            if (cart == null || cart.getUser() == null) {
                throw new IllegalArgumentException("Cart or user information cannot be null");
            }

            User user = userRepo.findById(cart.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found exception"));
            cart.setUser(user);
            return cartRepo.save(cart);
        }catch (DataAccessException e) {
            logger.error("Database access error while creating product : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while inserting the product ", e);
            throw e;
        }
    }

    @Override
    public CartItem addCartItem(CartItem cartItem) {
        try {
            if (cartItem == null || cartItem.getCart() == null || cartItem.getProduct() == null) {
                throw new IllegalArgumentException("CartItem, cart, or product information cannot be null");
            }

            Cart cart = cartRepo.findById(cartItem.getCart().getId())
                    .orElseThrow(() -> new CartNotFoundException("Cart not found"));

            Product product = productRepo.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            if (product.getIsAvailable() == null || !product.getIsAvailable()) {
                throw new ProductNotFoundException("Product is not available");
            }

            //check whether the product is in Stock or not
            Inventory inventory = inventoryRepo.findByProductId(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found for this product"));

            int availableQty = inventory.getStockQuantity();
            int requestedQty = cartItem.getQuantity();

            CartItem existing = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId()).orElse(null);

            int totalRequested = requestedQty + (existing != null ? existing.getQuantity() : 0);

            if (availableQty < totalRequested) {
                throw new IllegalArgumentException("Only " + availableQty + " items in stock");
            }

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());



            CartItem existingItem = cartItemRepo.findByCartIdAndProductId(
                    cart.getId(), product.getId()).orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                return cartItemRepo.save(existingItem);
            } else {
                return cartItemRepo.save(cartItem);
            }
        }catch (DataAccessException e) {
            logger.error("Database access error while creating cart Item  : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while inserting the product ", e);
            throw e;
        }
    }

    @Override
    public List<CartItemDto> getAllCartItemsById(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        try {
            List<CartItem> items = cartItemRepo.findByCartUserId(userId);
            if (items.isEmpty()) {
                throw new CartNotFoundException("No items in cart");
            }
            return items.stream()
                    .map(item -> new CartItemDto(
                            item.getId(),
                            item.getProduct().getName(),
                            item.getProduct().getImageAddress(),
                            item.getQuantity(),
                            item.getPrice()))
                    .toList();
        }
        catch (DataAccessException e) {
            logger.error("Database access error retrieveng the cart Items : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while getting the cart Items by Id  ", e);
            throw e;
        }
    }

    @Override
    public String removeCartItem(int cartItemId) {
        if (cartItemId <= 0) {
            throw new IllegalArgumentException("Cart ID must be positive");
        }
        try {
            if (!cartItemRepo.existsById(cartItemId)) {
                throw new CartNotFoundException("Item not found in cart");
            }
            cartItemRepo.deleteById(cartItemId);
            return "Item removed successfully";


    } catch (DataAccessException e) {
        logger.error("Database access while deleting  the cart Items : {}", e.getMessage(), e); // logs full stack trace

        throw new DataBaseException("Internal server error");
    }
        catch (Exception e) {
        log.error("Unexpected error while deleting  the cart Items by Id  ", e);
        throw e;
    }
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {

            if (coupon == null || coupon.getCode() == null) {
                throw new IllegalArgumentException("Coupon code cannot be null");
            }

            if (couponRepo.findByCode(coupon.getCode().trim()).isPresent()) {
                throw new CouponAlreadyExistsException("Coupon already exists");
            }

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
//
//    @Autowired
//    private CartMapper cartMapper;
//
//    @Autowired
//    private CartItemMapper cartItemMapper;
//
//    @Autowired
//    private CouponMapper couponMapper;
//
//    @Autowired
//    private AppliedCouponMapper appliedCouponMapper;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//
//    @Override
//    //from json id,userId
//    public Cart addToCart(Cart cart) {
//        try {
//            User user = userMapper.getUserByID(cart.getId());
//            if (user != null) {
//                int res = cartMapper.createCart(cart);
//                if (res <= 0) {
//                    throw new UserNotFoundException("User does not exists to add to the cart");
//                }
//            }
//
//            return cart;
//        } catch (DataRetrievalException e) {
//            throw new DataRetrievalException("Data could not be retrieved");
//        }
//    }
//
//
//    //now inside the cart we are gonna add the cart item
//    @Override
//    public CartItem addToCartItem(CartItem cartItem) {
//        //check the cartId and the productId
//        try {
//            Cart cart = cartMapper.getCartById(cartItem.getId());
//            if (cart == null) {
//                throw new CartNotFoundException("Cart does not exists");
//            }
//            Product product = productMapper.getProductById(cartItem.getProduct());
//            if (product == null) {
//                throw new ProductNotFoundException("Product could not be found");
//            }
//
//
//            if (product.getIsAvailable() == null || !product.getIsAvailable()) {
//                throw new ProductNotFoundException("Product is not available for purchase.");
//            }
//            //set the price from the product table
//            cartItem.setPrice(product.getPrice());
//
//
//            // Check if the same product already exists in the cart
//            CartItem existingItem = cartItemMapper.getCartItemByCartAndProduct(cartItem.getCartId(), cartItem.getProductId());
//
//
//            if (existingItem != null) {
//
//                // Update quantity if already present
//
//                int newQuantity = existingItem.getQuantity() + cartItem.getQuantity();
//
//                existingItem.setQuantity(newQuantity);
//                existingItem.setPrice(product.getPrice());
//
//                int updated = cartItemMapper.updateCartItem(existingItem);
//                if (updated <= 0) {
//                    throw new DataUpdateException("Failed to update cart item.");
//                }
//
//                return existingItem;
//            } else {
//
//                //Insert new cart item
//
//                int inserted = cartItemMapper.insertCartItem(cartItem);
//                if (inserted <= 0) {
//                    throw new DataCreationException("Failed to add item to cart.");
//                }
//                return cartItem;
//            }
//        } catch (DataCreationException e) {
//            throw new DataCreationException("data cannot be created");
//        }
//    }
//
//
//    //if I am going to retrieve all the cart Items based on the User Id I should
//    //have the product name,image details ,based on the productId kind of
//
//    @Override
//    public List<CartItemDto> getAllCartItemsById(int user_id) {
//        try {
//            List<CartItemDto> cartItem = cartItemMapper.getItemsByUserId(user_id);
//            if (cartItem == null)
//            {
//                throw new CartNotFoundException("Cart Item does not exists");
//            }
//            return cartItem;
//        }
//        catch (DataRetrievalException e)
//        {
//            throw new DataRetrievalException("Data could not be retrieved");
//        }
//    }
//
//
//    @Override
//    public String removeCartItem(int cart_item_id) {
//        try {
//            int deletedItem = cartItemMapper.deleteOneCartItem(cart_item_id);
//            if (deletedItem <= 0) {
//                throw new DataDeletionException("Could not delete the cart Item");
//            }
//
//            return "Item  removes Successfully";
//        } catch (DataRetrievalException e) {
//            throw new DataRetrievalException("Data could not be retrieved");
//        }
//    }
//
//
//    //remove all the Cart items

//    //from json the coupon entity to be sent
//    @Override
//    public Coupon addCoupons(Coupon coupon) {
//
//        if(coupon.getCode()==null && coupon==null)
//        {
//            throw new RuntimeException("The coupon code and coupon data cannot be null");
//        }
//       Coupon existingCoupon = couponMapper.getByCode(coupon.getCode());
//       if(existingCoupon!=null)
//       {
//           throw new CouponAlreadyExistsException("Coupon already exists");
//       }
//        try {
//            int res = couponMapper.insertCoupon(coupon);
//            if (res <= 0) {
//                throw new DataCreationException("Coupon was not be created");
//            }
//            return coupon;
//        } catch (DataRetrievalException e) {
//            throw new DataRetrievalException("Data could not be retrieved");
//        }
//    }
//
//    @Override
//    public List<Coupon> getAllAvailableCoupons() {
//        try {
//            return couponMapper.getAllCoupons();
//        } catch (DataRetrievalException e) {
//            throw new DataRetrievalException("Data could not be retrieved");
//        }
//    }
//
//
//    //apply coupon based on the orderId and the couponId that are present and store the data in the AppliedCoupon table
//    @Override
//    public AppliedCoupon ApplyCoupon(AppliedCoupon appliedCoupon) {
//
//        try {
//            Coupon coupon = couponMapper.getById(appliedCoupon.getCouponId());
//            if (coupon == null) {
//                throw new CouponNotFoundException("Coupon does not exists");
//            }
//            if ((coupon.getDiscountAmount() != null && coupon.getDiscountPercent() != null) ||
//                    (coupon.getDiscountAmount() == null && coupon.getDiscountPercent() == null)) {
//                throw new InvalidCouponException("Set either discount percent or discount amount, but not both.");
//            }
//            int res = appliedCouponMapper.insertAppliedCoupon(appliedCoupon);
//            if (res <= 0)
//            {
//                throw new DataCreationException("Could not apply the coupon");
//            }
//
//            return appliedCoupon;
//
//        } catch (DataRetrievalException e) {
//            throw new DataRetrievalException("Data could not be created");
//        }
//    }
//
//    @Override
//    public String removeCoupon(int appliedCoupon_Id) {
//        try {
//            AppliedCoupon appliedCoupon = appliedCouponMapper.getAppliedCouponById(appliedCoupon_Id);
//            if (appliedCoupon == null) {
//                throw new CouponNotFoundException("Coupon is not applied");
//            }
//            appliedCouponMapper.deleteAppliedCoupon(appliedCoupon_Id);
//            return "Coupon deleted successfully";
//        } catch (DataDeletionException e) {
//            throw new DataRetrievalException("Data could not be deleted");
//        }
//
//    }
//
//
//    @Override
//    public CartAmountSummaryDto calculateCartSummary(int userId) {
//        // 1. Get cart for user
//        try {
//            Cart cart = cartMapper.getCartByUserId(userId);
//            if (cart == null) {
//                throw new CartNotFoundException("Cart not found for user");
//            }
//
//            // 2. Get all cart items
//            List<CartItem> items = cartItemMapper.getItemsByCartId(cart.getId());
//            if (items == null || items.isEmpty()) {
//                throw new CartNotFoundException("No items in cart");
//            }
//
//            BigDecimal subtotal = BigDecimal.ZERO;
//
//            // 3. Calculate subtotal = sum(price × quantity)
//            for (CartItem item : items) {
//                BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
//                subtotal = subtotal.add(itemTotal);
//            }
//
//            // 4. Check for applied coupon
//            AppliedCoupon appliedCoupon = appliedCouponMapper.getAppliedCouponByCartId(cart.getId());
//            BigDecimal discount = BigDecimal.ZERO;
//            String couponCode = null;
//
//            if (appliedCoupon != null) {
//                Coupon coupon = couponMapper.getById(appliedCoupon.getCouponId());
//                if (coupon != null) {
//                    couponCode = coupon.getCode();
//
//                    // Percent discount
//                    if (coupon.getDiscountPercent() != null && coupon.getDiscountAmount() == null) {
//                        discount = subtotal.multiply(coupon.getDiscountPercent())
//                                .divide(BigDecimal.valueOf(100));
//                    }
//                    // Flat amount discount
//                    else if (coupon.getDiscountAmount() != null && coupon.getDiscountPercent() == null) {
//                        discount = coupon.getDiscountAmount();
//                    }
//                    // Safety check: if both are set, ignore both or throw error
//                    else if (coupon.getDiscountAmount() != null && coupon.getDiscountPercent() != null) {
//                        throw new RuntimeException("Invalid coupon: both discount types set.");
//                    }
//                }
//            }
//
//
//            // Tax and Shipping (example: 10% tax, ₹50 flat shipping)
//            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.10));  // 10% tax
//            BigDecimal shipping = BigDecimal.valueOf(50);                  // ₹50 shipping
//
//            // Final total
//            BigDecimal totalAmount = subtotal.subtract(discount).add(tax).add(shipping);
//
//            // Build response DTO
//            CartAmountSummaryDto summary = new CartAmountSummaryDto();
//            summary.setSubtotal(subtotal);
//            summary.setDiscount(discount);
//            summary.setTax(tax);
//            summary.setShipping(shipping);
//            summary.setTotalAmount(totalAmount);
//            summary.setCouponCode(couponCode);
//            return summary;
//        }
//        catch (DataRetrievalException e) {
//            throw new DataRetrievalException("Data could not be deleted");
//        }
//
//    }
//}
