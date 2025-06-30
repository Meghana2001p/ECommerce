package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Mapper.*;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.dto.CartAmountSummaryDto;
import com.project.E_Commerce.dto.CartItemDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.channels.ScatteringByteChannel;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private AppliedCouponMapper appliedCouponMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;


    @Override
    //from json id,userId
    public Cart addToCart(Cart cart) {
        try {
            User user = userMapper.getUserByID(cart.getUserId());
            if (user != null) {
                int res = cartMapper.createCart(cart);
                if (res <= 0) {
                    throw new UserNotFoundException("User does not exists to add to the cart");
                }
            }

            return cart;
        } catch (DataRetrievalException e) {
            throw new DataRetrievalException("Data could not be retrieved");
        }
    }


    //now inside the cart we are gonna add the cart item
    @Override
    public CartItem addToCartItem(CartItem cartItem) {
        //check the cartId and the productId
        try {
            Cart cart = cartMapper.getCartById(cartItem.getCartId());
            if (cart == null) {
                throw new CartNotFoundException("Cart does not exists");
            }
            Product product = productMapper.getProductById(cartItem.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Product could not be found");
            }


            if (product.getIsAvailable() == null || !product.getIsAvailable()) {
                throw new ProductNotFoundException("Product is not available for purchase.");
            }
            //set the price from the product table
            cartItem.setPrice(product.getPrice());


            // Check if the same product already exists in the cart
            CartItem existingItem = cartItemMapper.getCartItemByCartAndProduct(cartItem.getCartId(), cartItem.getProductId());


            if (existingItem != null) {

                // Update quantity if already present

                int newQuantity = existingItem.getQuantity() + cartItem.getQuantity();

                existingItem.setQuantity(newQuantity);
                existingItem.setPrice(product.getPrice());

                int updated = cartItemMapper.updateCartItem(existingItem);
                if (updated <= 0) {
                    throw new DataUpdateException("Failed to update cart item.");
                }

                return existingItem;
            } else {

                //Insert new cart item

                int inserted = cartItemMapper.insertCartItem(cartItem);
                if (inserted <= 0) {
                    throw new DataCreationException("Failed to add item to cart.");
                }
                return cartItem;
            }
        } catch (DataCreationException e) {
            throw new DataCreationException("data cannot be created");
        }
    }


    //if I am going to retrieve all the cart Items based on the User Id I should
    //have the product name,image details ,based on the productId kind of

    @Override
    public List<CartItemDto> getAllCartItemsById(int user_id) {
        try {
            List<CartItemDto> cartItem = cartItemMapper.getItemsByUserId(user_id);
            if (cartItem == null)
            {
                throw new CartNotFoundException("Cart Item does not exists");
            }
            return cartItem;
        }
        catch (DataRetrievalException e)
        {
            throw new DataRetrievalException("Data could not be retrieved");
        }
    }


    @Override
    public String removeCartItem(int cart_item_id) {
        try {
            int deletedItem = cartItemMapper.deleteOneCartItem(cart_item_id);
            if (deletedItem <= 0) {
                throw new DataDeletionException("Could not delete the cart Item");
            }

            return "Item  removes Successfully";
        } catch (DataRetrievalException e) {
            throw new DataRetrievalException("Data could not be retrieved");
        }
    }


    //remove all the Cart items

















    //from json the coupon entity to be sent
    @Override
    public Coupon addCoupons(Coupon coupon) {

        if(coupon.getCode()==null && coupon==null)
        {
            throw new RuntimeException("The coupon code and coupon data cannot be null");
        }
       Coupon existingCoupon = couponMapper.getByCode(coupon.getCode());
       if(existingCoupon!=null)
       {
           throw new CouponAlreadyExistsException("Coupon already exists");
       }
        try {
            int res = couponMapper.insertCoupon(coupon);
            if (res <= 0) {
                throw new DataCreationException("Coupon was not be created");
            }
            return coupon;
        } catch (DataRetrievalException e) {
            throw new DataRetrievalException("Data could not be retrieved");
        }
    }

    @Override
    public List<Coupon> getAllAvailableCoupons() {
        try {
            return couponMapper.getAllCoupons();
        } catch (DataRetrievalException e) {
            throw new DataRetrievalException("Data could not be retrieved");
        }
    }


    //apply coupon based on the orderId and the couponId that are present and store the data in the AppliedCoupon table
    @Override
    public AppliedCoupon ApplyCoupon(AppliedCoupon appliedCoupon) {

        try {
            Coupon coupon = couponMapper.getById(appliedCoupon.getCouponId());
            if (coupon == null) {
                throw new CouponNotFoundException("Coupon does not exists");
            }
            if ((coupon.getDiscountAmount() != null && coupon.getDiscountPercent() != null) ||
                    (coupon.getDiscountAmount() == null && coupon.getDiscountPercent() == null)) {
                throw new InvalidCouponException("Set either discount percent or discount amount, but not both.");
            }
            int res = appliedCouponMapper.insertAppliedCoupon(appliedCoupon);
            if (res <= 0)
            {
                throw new DataCreationException("Could not apply the coupon");
            }

            return appliedCoupon;

        } catch (DataRetrievalException e) {
            throw new DataRetrievalException("Data could not be created");
        }
    }

    @Override
    public String removeCoupon(int appliedCoupon_Id) {
        try {
            AppliedCoupon appliedCoupon = appliedCouponMapper.getAppliedCouponById(appliedCoupon_Id);
            if (appliedCoupon == null) {
                throw new CouponNotFoundException("Coupon is not applied");
            }
            appliedCouponMapper.deleteAppliedCoupon(appliedCoupon_Id);
            return "Coupon deleted successfully";
        } catch (DataDeletionException e) {
            throw new DataRetrievalException("Data could not be deleted");
        }

    }


    @Override
    public CartAmountSummaryDto calculateCartSummary(int userId) {
        // 1. Get cart for user
        try {
            Cart cart = cartMapper.getCartByUserId(userId);
            if (cart == null) {
                throw new CartNotFoundException("Cart not found for user");
            }

            // 2. Get all cart items
            List<CartItem> items = cartItemMapper.getItemsByCartId(cart.getId());
            if (items == null || items.isEmpty()) {
                throw new CartNotFoundException("No items in cart");
            }

            BigDecimal subtotal = BigDecimal.ZERO;

            // 3. Calculate subtotal = sum(price × quantity)
            for (CartItem item : items) {
                BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                subtotal = subtotal.add(itemTotal);
            }

            // 4. Check for applied coupon
            AppliedCoupon appliedCoupon = appliedCouponMapper.getAppliedCouponByCartId(cart.getId());
            BigDecimal discount = BigDecimal.ZERO;
            String couponCode = null;

            if (appliedCoupon != null) {
                Coupon coupon = couponMapper.getById(appliedCoupon.getCouponId());
                if (coupon != null) {
                    couponCode = coupon.getCode();

                    // Percent discount
                    if (coupon.getDiscountPercent() != null && coupon.getDiscountAmount() == null) {
                        discount = subtotal.multiply(coupon.getDiscountPercent())
                                .divide(BigDecimal.valueOf(100));
                    }
                    // Flat amount discount
                    else if (coupon.getDiscountAmount() != null && coupon.getDiscountPercent() == null) {
                        discount = coupon.getDiscountAmount();
                    }
                    // Safety check: if both are set, ignore both or throw error
                    else if (coupon.getDiscountAmount() != null && coupon.getDiscountPercent() != null) {
                        throw new RuntimeException("Invalid coupon: both discount types set.");
                    }
                }
            }


            // Tax and Shipping (example: 10% tax, ₹50 flat shipping)
            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.10));  // 10% tax
            BigDecimal shipping = BigDecimal.valueOf(50);                  // ₹50 shipping

            // Final total
            BigDecimal totalAmount = subtotal.subtract(discount).add(tax).add(shipping);

            // Build response DTO
            CartAmountSummaryDto summary = new CartAmountSummaryDto();
            summary.setSubtotal(subtotal);
            summary.setDiscount(discount);
            summary.setTax(tax);
            summary.setShipping(shipping);
            summary.setTotalAmount(totalAmount);
            summary.setCouponCode(couponCode);
            return summary;
        }
        catch (DataRetrievalException e) {
            throw new DataRetrievalException("Data could not be deleted");
        }

    }
}
