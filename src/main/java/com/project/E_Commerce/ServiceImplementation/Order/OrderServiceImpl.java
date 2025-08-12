package com.project.E_Commerce.ServiceImplementation.Order;

import com.project.E_Commerce.Entity.Email.NotificationQueue;
import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Order.OrderItem;
import com.project.E_Commerce.Entity.Payment.Payment;
import com.project.E_Commerce.Entity.Payment.Transaction;
import com.project.E_Commerce.Entity.Product.Inventory;
import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.User.User;
import com.project.E_Commerce.Repository.Cart.AppliedCouponRepo;
import com.project.E_Commerce.Repository.Cart.CartItemRepo;
import com.project.E_Commerce.Repository.Cart.CartRepo;
import com.project.E_Commerce.Repository.Notification.NotificationQueueRepo;
import com.project.E_Commerce.Repository.Order.*;
import com.project.E_Commerce.Repository.Payment.PaymentRepo;
import com.project.E_Commerce.Repository.Payment.TransactionRepo;
import com.project.E_Commerce.Repository.Product.InventoryRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.User.UserRepo;
import com.project.E_Commerce.Service.Cart.CartService;
import com.project.E_Commerce.Service.Order.OrderService;
import com.project.E_Commerce.dto.Cart.CartItemResponse;
import com.project.E_Commerce.dto.Cart.CartResponse;
import com.project.E_Commerce.dto.Order.OrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private DeliveryStatusRepo deliveryStatusRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private AppliedCouponRepo appliedCouponRepo;


    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private GiftOrderRepo giftOrderRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private NotificationQueueRepo notificationQueueRepo;

    @Autowired
    private OrderStatusHistoryRepo orderStatusHistoryRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TransactionRepo transactionRepo;


@Transactional
@Override
public String placeOrder(OrderRequest orderRequest) {
    if (orderRequest == null) {
        throw new IllegalArgumentException("Invalid order request");
    }

    Integer userId = orderRequest.getUserId();
    User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    CartResponse cartResponse = cartService.viewCart(userId);
    if (cartResponse.getCartItems() == null || cartResponse.getCartItems().isEmpty()) {
        throw new IllegalArgumentException("Cart is empty");
    }

    BigDecimal grandTotal = cartResponse.getSummary().getGrandTotal();

    // Create order
    Order order = new Order();
    order.setUser(user);
    order.setShippingAddress(orderRequest.getShippingAddress());
    order.setOrderDate(LocalDateTime.now());
    order.setTotalAmount(grandTotal);
    order.setPhoneNumber(orderRequest.getPhoneNumber());
    order.setPaymentMethod(orderRequest.getPaymentMethod());
    order.setGift(orderRequest.getIsGift());
    order.setCreatedAt(LocalDateTime.now());

    // Initialize payment
    Payment payment = new Payment();
    payment.setAmount(grandTotal);
    payment.setPaidAt(LocalDateTime.now());

    Order.PaymentMethod paymentMethod = orderRequest.getPaymentMethod();
    if (paymentMethod == Order.PaymentMethod.COD) {
        order.setOrderStatus(Order.OrderStatus.CONFIRMED);
        payment.setStatus(Payment.PaymentStatus.PENDING);
    } else {
        order.setPaymentMethod(Order.PaymentMethod.UPI);
        payment.setStatus(Payment.PaymentStatus.PENDING);

        Transaction transaction = transactionRepo.findByUserId(userId);
        if (transaction != null) {
            payment.setTransactionId(transaction.getTransactionId());
            order.setOrderStatus(Order.OrderStatus.CONFIRMED);
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
        } else {
            order.setOrderStatus(Order.OrderStatus.CONFIRMED);
        }

    }

    // Save order first to generate order ID
    orderRepo.save(order);

    // Set order in payment and save
    payment.setOrder(order);
    paymentRepo.save(payment);

    // ✅ Save OrderItems and update inventory
    List<OrderItem> orderItems = new ArrayList<>();
    for (CartItemResponse itemDto : cartResponse.getCartItems()) {
        Product product = productRepo.findById(itemDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Create OrderItem
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(itemDto.getQuantity());
        item.setPrice(product.getPrice());

        orderItems.add(item);

        // Update Inventory
        Inventory inventory = inventoryRepo.findByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for product ID: " + product.getId()));

        int newStock = inventory.getStockQuantity() - itemDto.getQuantity();
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        inventory.setStockQuantity(newStock);
        inventory.setInStock(newStock > 0);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepo.save(inventory);
    }

    orderItemRepo.saveAll(orderItems);

    //  Clear cart after order placement
    cartService.clearCart(userId);

    //Queue Email Notification
    NotificationQueue emailNotification = new NotificationQueue();
    emailNotification.setUser(user);
    emailNotification.setType(NotificationQueue.NotificationType.EMAIL);
    emailNotification.setStatus(NotificationQueue.NotificationStatus.PENDING);
    emailNotification.setScheduledAt(LocalDateTime.now().plusMinutes(30));
    String message = "Hey " + user.getName() + ", your order (ID: " + order.getId() + ") has been successfully placed! 🎉\n\n" +
            "We'll notify you once it's packed and on its way.\n" +
            "Thank you for shopping with us! 🛍️\n\n" +
            "Track your order anytime from your Orders section.";

    emailNotification.setMessage(message);

    notificationQueueRepo.save(emailNotification);

    return "Order placed with ID: " + order.getId();
}

    @Transactional
    @Override
    public String updateDeliveryStatusByAgent(Integer orderId, Order.OrderStatus newStatus) {

        // 1. Fetch the order
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Prevent changing status if already DELIVERED or CANCELLED
        if (order.getOrderStatus() == Order.OrderStatus.DELIVERED || order.getOrderStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status after delivery or cancellation.");
        }


        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);



        //Send email notification if status is DELIVERED
        if (newStatus == Order.OrderStatus.DELIVERED) {
            User user = userRepo.findById(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found for order ID: " + orderId));

            NotificationQueue emailNotification = new NotificationQueue();
            emailNotification.setUser(user);
            emailNotification.setType(NotificationQueue.NotificationType.EMAIL);
            emailNotification.setStatus(NotificationQueue.NotificationStatus.PENDING);
            emailNotification.setScheduledAt(LocalDateTime.now().plusMinutes(3000));

            String message = "Hey " + user.getName() + ", your order (ID: " + order.getId() + ") has been successfully delivered! 📦\n\n" +
                    "We hope you love it. If you have any feedback, let us know!\n\n" +
                    "Thanks again for shopping with us! 🛍️";

            emailNotification.setMessage(message);
            notificationQueueRepo.save(emailNotification);
        }
        return  "Order updated successfully";
    }

    @Override
    public String cancelOrderByUser(Integer orderId, Integer userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Ownership check
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Unauthorized: This order doesn't belong to you.");
        }

        // Prevent cancel if already delivered or cancelled
        if (order.getOrderStatus() == Order.OrderStatus.DELIVERED ||
                order.getOrderStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order already delivered or cancelled.");
        }

        // 1. Update order status
        order.setOrderStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);

        // 2. Update payment table
        Optional<Payment> paymentOpt = paymentRepo.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            if (order.getPaymentMethod() == null) {
                throw new IllegalStateException("Payment method not specified.");
            }


            if (order.getPaymentMethod() == Order.PaymentMethod.COD) {
                payment.setStatus(Payment.PaymentStatus.FAILED);
            } else {
                // Initiate refund
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
            }

            paymentRepo.save(payment);
        }



        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationQueue emailNotification = new NotificationQueue();
        emailNotification.setUser(user);
        emailNotification.setType(NotificationQueue.NotificationType.EMAIL);
        emailNotification.setStatus(NotificationQueue.NotificationStatus.PENDING);
        emailNotification.setScheduledAt(LocalDateTime.now().plusMinutes(400));
        String message = "Hey " + user.getName() + ", your order (ID: " + orderId + ") has been cancelled as per your request. ❌\n\n" +
                "If any payment was made, the refund will be initiated shortly.\n\nThanks for using our platform!";
        emailNotification.setMessage(message);
        notificationQueueRepo.save(emailNotification);
        return "Order Cancelled successfully";
    }


    @Override
    public Order getOrderById(int orderId) {


        if(orderId<=0)
        {
            throw new IllegalArgumentException("Delivery Status should not be null");
        }

          return orderRepo.findById(orderId)
                  .orElseThrow(() -> new RuntimeException("Order not found"));

    }

    @Override
    public List<Order> getAllOrders() {

        return orderRepo.findAll();

    }
}

