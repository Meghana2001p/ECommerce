package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.*;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.CartService;
import com.project.E_Commerce.Service.OrderService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private  GiftOrderRepo giftOrderRepo;

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



//    @Transactional
//    @Override
//    public String placeOrder(OrderRequest orderRequest) {
//        // Existing validations...
//
//        // Save Order and OrderItems
//        orderRepo.save(order);
//        orderItemRepo.saveAll(orderItems);
//
//        // ✅ Update Inventory
//        for (OrderItem item : orderItems) {
//            Integer productId = item.getProduct().getId();
//            Integer quantityOrdered = item.getQuantity();
//
//            Inventory inventory = inventoryRepo.findByProductId(productId)
//                    .orElseThrow(() -> new RuntimeException("Inventory not found for product ID: " + productId));
//
//            int updatedStock = inventory.getStockQuantity() - quantityOrdered;
//            if (updatedStock < 0) {
//                throw new RuntimeException("Insufficient stock for product ID: " + productId);
//            }
//
//            inventory.setStockQuantity(updatedStock);
//            inventory.setInStock(updatedStock > 0);
//            inventory.setLastUpdated(LocalDateTime.now());
//
//            inventoryRepo.save(inventory);
//        }
//
//        // ✅ (Optional Next Step) Send Email Notification
//
//        return "Order placed successfully with ID: " + order.getId();
//    }

@Transactional
@Override
public String placeOrder(OrderRequest orderRequest) {
    if (orderRequest == null) {
        throw new IllegalArgumentException("Invalid request");
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
    payment.setAmount(grandTotal); // set payment amount
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
            order.setOrderStatus(Order.OrderStatus.CANCELLED);
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }
    }

    // Save order first to generate order ID
    orderRepo.save(order);

    // Set order in payment and save
    payment.setOrder(order);
    paymentRepo.save(payment);

    // You can return an order ID or confirmation message
    return "Order placed with ID: " + order.getId();
}



    @Override
    public String updateDeliveryStatusByAgent(DeliveryStatusAgentUpdateDto dto) {
        if(dto==null)
        {
            throw new IllegalArgumentException("Delivery Status should not be null");
        }
      try {
          int updated = deliveryStatusRepo.updateDeliveryStatusByAgent(dto.getOrderId(), dto.getStatus(), LocalDateTime.now());
          if (updated <= 0) {
              return "Delivery status update failed for Order ID: " + dto.getOrderId();
          }
          return "Delivery status updated successfully for Order ID: " + dto.getOrderId();
      }catch (DataAccessException e) {
          logger.error("Database access while  updating the delivery by the Agent   : {}", e.getMessage(), e); // logs full stack trace

          throw new DataBaseException("Internal server error");
      }
      catch (Exception e) {
          log.error("Unexpected error while while  updating the delivery by the Agent   ", e);
          throw e;
      }
    }


    @Override
    public void updateDeliveryStatusByAdmin(DeliveryStatusAdminUpdateDto dto) {
        if(dto==null)
        {
            throw new IllegalArgumentException("Delivery Status should not be null");
        }

      try {

          int updated = deliveryStatusRepo.updateDeliveryStatusByAdmin(dto.getOrderId(), dto.getStatus(), dto.getCarrier());
          if (updated <= 0) {
              throw new DataUpdateException("Delivery status update failed");
          }

          if (dto.getStatus() == DeliveryStatus.DeliveryState.DELIVERED) {
              Order order = orderRepo.findById(dto.getOrderId())
                      .orElseThrow(() -> new RuntimeException("Order not found"));

              OrderStatusHistory history = new OrderStatusHistory();
              history.setOrder(order);
              history.setStatus(OrderStatusHistory.OrderStatus.DELIVERED);
              history.setUpdatedAt(LocalDateTime.now());
              orderStatusHistoryRepo.save(history);

              NotificationQueue notification = new NotificationQueue();
              notification.setUser(order.getUser());
              notification.setType(NotificationQueue.NotificationType.EMAIL);
              notification.setMessage("📦 Your order #" + order.getId() + " has been delivered. Thank you!");
              notification.setStatus(NotificationQueue.NotificationStatus.PENDING);
              notification.setScheduledAt(LocalDateTime.now());
              notificationQueueRepo.save(notification);
          }
      }catch (DataAccessException e) {
          logger.error("Database access while  updating the delivery by the Admin   : {}", e.getMessage(), e); // logs full stack trace

          throw new DataBaseException("Internal server error");
      }
      catch (Exception e) {
          log.error("Unexpected error while while  updating the delivery by the Admin   ", e);
          throw e;
      }

    }

    @Override
    public String updateOrderByUser(UserOrderUpdateDto dto) {

        if(dto==null)
        {
            throw new IllegalArgumentException("Update Order should not be null");
        }
      try {
          Order order = orderRepo.findById(dto.getOrderId())
                  .orElseThrow(() -> new RuntimeException("Order not found"));

          if (Boolean.TRUE.equals(dto.getCancelOrder()) &&
                  (order.getOrderStatus() == Order.OrderStatus.SHIPPED || order.getOrderStatus() == Order.OrderStatus.DELIVERED)) {
              throw new OrderCannotCancelException("Order is already shipped or delivered.");
          }

          order.setOrderStatus(Order.OrderStatus.CANCELLED);
          orderRepo.save(order);

          OrderStatusHistory history = new OrderStatusHistory();
          history.setOrder(order);
          history.setStatus(OrderStatusHistory.OrderStatus.CANCELLED);
          history.setUpdatedAt(LocalDateTime.now());
          orderStatusHistoryRepo.save(history);

          List<OrderItem> orderItems = orderItemRepo.findByOrderId(order.getId());
          for (OrderItem item : orderItems) {
              Inventory inventory = inventoryRepo.findByProductId(item.getProduct().getId())
                      .orElseThrow(() -> new IllegalStateException("Inventory not found for product: " + item.getProduct().getId()));

              int updatedStock = inventory.getStockQuantity() + item.getQuantity();
              inventory.setStockQuantity(updatedStock);
              inventory.setInStock(true);
              inventory.setLastUpdated(LocalDateTime.now());
              inventoryRepo.save(inventory);
          }
          return "Order cancelled successfully.";
      }
      catch (DataAccessException e) {
          logger.error("Database access while  updating the Order by the user    : {}", e.getMessage(), e); // logs full stack trace

          throw new DataBaseException("Internal server error");
      }
      catch (Exception e) {
          log.error("Unexpected error while while  updating the Order  by the user  ", e);
          throw e;
      }
    }



    @Override
    public Order getOrderById(int orderId) {


        if(orderId<=0)
        {
            throw new IllegalArgumentException("Delivery Status should not be null");
        }
      try {
          return orderRepo.findById(orderId)
                  .orElseThrow(() -> new RuntimeException("Order not found"));
      }
      catch (DataAccessException e) {
          logger.error("Database access while  getting the order data  : {}", e.getMessage(), e); // logs full stack trace

          throw new DataBaseException("Internal server error");
      }
      catch (Exception e) {
          log.error("Unexpected error while while  getting the order data ", e);
          throw e;
      }
    }

    @Override
    public List<Order> getAllOrders() {
        try {
            return orderRepo.findAll();

        } catch (DataAccessException e) {
            logger.error("Database access while  getting  all the order data  : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            log.error("Unexpected error while while  getting  all the order data ", e);
            throw e;
        }
    }
}

