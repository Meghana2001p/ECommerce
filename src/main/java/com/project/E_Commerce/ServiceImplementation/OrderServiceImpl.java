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





  @Transactional
    @Override
    public OrderPlacedResponseDto placeOrder(Order orderRequest) {
      if(orderRequest==null)
      {
          throw new IllegalArgumentException("Order request should not be null");
      }
      try {
          if (orderRequest.getUser().getId() == null) {
              throw new IllegalArgumentException("User ID cannot be null");
          }

          // Get cart summary
          CartAmountSummaryDto summary = cartService.calculateCartSummary(orderRequest.getUser().getId());
          if (summary.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
              throw new OrderCouldNotBePlacedException("Cart is empty");
          }

          // Create order
          orderRequest.setOrderDate(LocalDateTime.now());
          orderRequest.setCreatedAt(LocalDateTime.now());
          orderRequest.setOrderStatus(Order.OrderStatus.PENDING);
          orderRequest.setTotalAmount(summary.getTotalAmount());

          Order savedOrder = orderRepo.save(orderRequest);

          // Get cart and items
          Cart cart = cartRepo.findByUserId(orderRequest.getUser().getId())
                  .orElseThrow(() -> new CartNotFoundException("Cart not found"));

          List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());
          if (cartItems.isEmpty()) throw new CartNotFoundException("No items in cart");

          for (CartItem item : cartItems) {
              // Save OrderItem
              OrderItem orderItem = new OrderItem();
              orderItem.setOrder(savedOrder);
              orderItem.setProduct(item.getProduct());
              orderItem.setQuantity(item.getQuantity());
              orderItem.setPrice(item.getPrice());
              orderItemRepo.save(orderItem);

              // Deduct inventory
              Inventory inventory = inventoryRepo.findByProductId(item.getProduct().getId())
                      .orElseThrow(() -> new IllegalStateException("Inventory not found for product: " + item.getProduct().getId()));

              int remaining = inventory.getStockQuantity() - item.getQuantity();
              if (remaining < 0) {
                  throw new IllegalArgumentException("Insufficient stock for product: " + item.getProduct().getName());
              }

              inventory.setStockQuantity(remaining);
              inventory.setInStock(remaining > 0);
              inventory.setLastUpdated(LocalDateTime.now());
              inventoryRepo.save(inventory);
          }

          // Save payment
          String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
          Payment payment = new Payment();
          payment.setOrder(savedOrder);
          payment.setAmount(savedOrder.getTotalAmount());
          payment.setPaidAt(LocalDateTime.now());
          payment.setStatus(Payment.PaymentStatus.PENDING);
          payment.setPaymentMethod(Payment.PaymentMethod.valueOf(orderRequest.getPaymentMethod().name()));
          payment.setTransactionId(trackingNumber);
          paymentRepo.save(payment);

          // Save gift order if applicable
          if (orderRequest.isGift()) {
              GiftOrder giftOrder = new GiftOrder();
              giftOrder.setOrder(savedOrder);
              giftOrder.setGiftMessage("Happy Shopping!");
              giftOrder.setGiftWrapping(true);
              giftOrder.setHidePrice(true);
              giftOrderRepo.save(giftOrder);
          }

          // Create delivery status
          DeliveryStatus deliveryStatus = new DeliveryStatus();
          deliveryStatus.setOrder(savedOrder);
          deliveryStatus.setStatus(DeliveryStatus.DeliveryState.PENDING);
          deliveryStatus.setUpdatedAt(LocalDateTime.now());
          deliveryStatus.setTrackingNumber(trackingNumber);
          deliveryStatus.setCarrier("Blue-dart");
          deliveryStatus.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(5));
          deliveryStatus.setDeliveryType(DeliveryStatus.DeliveryType.STANDARD);
          deliveryStatusRepo.save(deliveryStatus);

          // Cleanup: clear cart & coupon
          cartItemRepo.deleteAll(cartItems);
          appliedCouponRepo.deleteByCartId(cart.getId());

          // Notification
          NotificationQueue notification = new NotificationQueue();
          notification.setUser(orderRequest.getUser());
          notification.setType(NotificationQueue.NotificationType.EMAIL);
          notification.setMessage("🎉 Your order #" + savedOrder.getId() + " has been placed! Track ID: " + trackingNumber);
          notification.setStatus(NotificationQueue.NotificationStatus.PENDING);
          notification.setScheduledAt(LocalDateTime.now());
          notificationQueueRepo.save(notification);

          return new OrderPlacedResponseDto(
                  "Order placed successfully!",
                  savedOrder.getId(),
                  trackingNumber,
                  deliveryStatus.getEstimatedDeliveryDate(),
                  savedOrder.getOrderStatus(),
                  savedOrder.getTotalAmount()
          );
      }catch (DataAccessException e) {
          logger.error("Database access while   Placing Order   : {}", e.getMessage(), e); // logs full stack trace

          throw new DataBaseException("Internal server error");
      }
      catch (Exception e) {
          log.error("Unexpected error while   Placing Order   ", e);
          throw e;
      }

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

    //the user is gonna place the order and the data is gonna be stored in the order table
    //and also  the response is gonna be there
    //and the data is gonna be stored in the database of the delivery status
//    @Override
//    @Transactional
//    public OrderPlacedResponseDto placeOrder(Order order) {
//        try {
//            if (order.getUserId() == null) {
//                throw new OrderValidationException("User ID cannot be null");
//            }
//            // Validate cart
//            CartAmountSummaryDto cartSummary = cartService.calculateCartSummary(order.getUserId());
//
//            if (cartSummary.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
//                throw new OrderCouldNotBePlacedException("Cart is empty");
//            }
//
//            order.setOrderDate(LocalDateTime.now());
//            order.setCreatedAt(LocalDateTime.now());
//            order.setOrderStatus(Order.OrderStatus.PENDING);
//            CartAmountSummaryDto cartAmountSummaryDto = cartService.calculateCartSummary(order.getUserId());
//            order.setTotalAmount(cartAmountSummaryDto.getTotalAmount());
//
//            //set the order date the time and also the total amount before saving it into the database
//            int res = orderMapper.insertOrder(order);
//
//            if (res <= 0) {
//                throw new OrderCouldNotBePlacedException("Order cannot be placed");
//            }
//
//
//            // Simulate delivery estimation and tracking generation (can later be real)
//            String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//            LocalDateTime estimatedDate = LocalDateTime.now().plusDays(5);
//
//
//            //  Step 3: Insert Order Items
//            Cart cart = cartMapper.getCartByUserId(order.getUserId());
//            List<CartItem> cartItems = cartItemMapper.getItemsByCartId(cart.getId());
//            for (CartItem item : cartItems) {
//                OrderItem orderItem = new OrderItem();
//                orderItem.setOrderId(order.getId());
//                orderItem.setProductId(item.getProductId());
//                orderItem.setQuantity(item.getQuantity());
//                orderItem.setPrice(item.getPrice());
//                orderItemMapper.insertOrderItem(orderItem);
//            }
//
//            // Step 4: Insert Payment Info
//            Payment payment = new Payment();
//            payment.setOrderId(order.getId());
//            payment.setAmount(order.getTotalAmount());
//            payment.setPaidAt(LocalDateTime.now());
//            payment.setStatus(Payment.PaymentStatus.PENDING); // Default: Pending
//            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(order.getPaymentMethod().name()));
//            payment.setTransactionId(trackingNumber);
//            paymentMapper.insertPayment(payment);
//
//            //  Step 5: Insert Gift Order if applicable
//            if (order.isGift()) {
//                GiftOrder giftOrder = new GiftOrder();
//                giftOrder.setOrderId(order.getId());
//                giftOrder.setGiftMessage("Happy Shopping!"); // default message
//                giftOrder.setGiftWrapping(true);
//                giftOrder.setHidePrice(true);
//                giftOrderMapper.insertGiftOrder(giftOrder);
//            }
//
//
//            DeliveryStatus deliveryStatus = new DeliveryStatus();
//            deliveryStatus.setOrderId(order.getId());
//            deliveryStatus.setStatus(DeliveryStatus.DeliveryState.PENDING);
//            deliveryStatus.setUpdatedAt(LocalDateTime.now());
//            deliveryStatus.setTrackingNumber(trackingNumber);
//            deliveryStatus.setCarrier("Blue-dart");
//            deliveryStatus.setEstimatedDeliveryDate(estimatedDate);
//            deliveryStatus.setDeliveryType(DeliveryStatus.DeliveryType.STANDARD);
//
//            int deliveryStatusRes = deliveryStatusMapper.insertDeliveryStatus(deliveryStatus);
//            if (deliveryStatusRes <= 0) {
//                throw new DeliveryStatusCreationException("delivery Status cannot be updated");
//            }
//
//            Cart cart1 = cartMapper.getCartByUserId(order.getUserId());
//            if (cart1 != null) {
//                int cartId = cart1.getId();
//
//                CartItem cartItem = cartItemMapper.getAnyCartItemByCartId(cartId);
//                if (cartItem != null) {
//                    cartItemMapper.clearCartByCartId(cartId);
//                }
//                appliedCouponMapper.deleteByCartId(cartId);
//
//            }
//            NotificationQueue notification = new NotificationQueue();
//            notification.setUserId(order.getUserId());
//            notification.setType(NotificationQueue.NotificationType.EMAIL);
//            notification.setMessage("🎉 Your order " + order.getId() + " has been placed! Track ID: " + trackingNumber);
//            notification.setStatus(NotificationQueue.NotificationStatus.PENDING);
//            notification.setScheduledAt(LocalDateTime.now());
//            notificationMapper.insertNotification(notification);
//
//
//
//            return new OrderPlacedResponseDto(
//                    "Order placed successfully!",
//                    order.getId(),
//                    trackingNumber,
//                    estimatedDate,
//                    order.getOrderStatus(),
//                    order.getTotalAmount()
//            );
//        }
//    catch (DataCreationException e)
//    {
//        throw new DataCreationException("Data cannot be created");
//    }
//
//    }
//
//
//
//    @Override
//    public String updateDeliveryStatusByAgent(DeliveryStatusAgentUpdateDto dto) {
//        try {
//            int rowsUpdated = deliveryStatusMapper.updateDeliveryStatus(dto);
//            if (rowsUpdated <= 0) {
//                return "Delivery status update failed for Order ID: " + dto.getOrderId();
//            }
//            return " Delivery status updated for Order ID: " + dto.getOrderId();
//        }
//        catch (DataUpdateException e)
//        {
//            throw new DataUpdateException("Data cannot be  Update");
//
//        }
//    }
//
//
//
//    @Override
//    public void updateDeliveryStatusByAdmin(DeliveryStatusAdminUpdateDto dto) {
//        try {
//            int updated = deliveryStatusMapper.updateDeliveryStatusByAdmin(dto);
//
//            if (updated <= 0) {
//                throw new RuntimeException("Failed to update delivery status for Order ID: " + dto.getOrderId());
//            }
//
//            if (dto.getStatus() == DeliveryStatus.DeliveryState.DELIVERED) {
//                Order order = orderMapper.getOrderById(dto.getOrderId());
//
//
//
//
//                OrderStatusHistory orderHistory = new OrderStatusHistory();
//                orderHistory.setOrderId(order.getId());
//                orderHistory.setStatus(OrderStatusHistory.OrderStatus.DELIVERED);
//                orderHistory.setUpdatedAt(LocalDateTime.now());
//                orderStatusHistoryMapper.insertStatusHistory(orderHistory);
//
//
//
//                NotificationQueue notification = new NotificationQueue();
//                notification.setUserId(order.getUserId());
//                notification.setType(NotificationQueue.NotificationType.EMAIL);
//                notification.setMessage("📦 Your order #" + dto.getOrderId() + " has been delivered. Thank you for shopping with us!");
//                notification.setStatus(NotificationQueue.NotificationStatus.PENDING);
//                notification.setScheduledAt(LocalDateTime.now());
//                notificationMapper.insertNotification(notification);
//            }
//        }
//        catch (DataUpdateException e)
//        {
//            throw new DataUpdateException("Data cannot be  Update");
//
//        }
//
//    }
//
//    @Override
//    public String updateOrderByUser(UserOrderUpdateDto dto) {
//        try {
//            Order existingOrder = orderMapper.getOrderById(dto.getOrderId());
//
//            if (existingOrder == null) {
//                throw new RuntimeException("Order not found.");
//            }
//
//            // Prevent cancel if already shipped or beyond
//            if (Boolean.TRUE.equals(dto.getCancelOrder()) &&
//                    (existingOrder.getOrderStatus() == Order.OrderStatus.SHIPPED ||
//                            existingOrder.getOrderStatus() == Order.OrderStatus.DELIVERED)) {
//                throw new OrderCannotCancelException("You cannot cancel an order that is already shipped or delivered.");
//            }
//
//            // 3. Update the order’s status to CANCELLED
//            existingOrder.setOrderStatus(Order.OrderStatus.CANCELLED);
//            orderMapper.updateOrderStatus(existingOrder.getId(),"CANCELLED");
//
//            // 4. Insert into history
//            OrderStatusHistory history = new OrderStatusHistory();
//            history.setOrderId(existingOrder.getId());
//            history.setStatus(OrderStatusHistory.OrderStatus.CANCELLED);
//            history.setUpdatedAt(LocalDateTime.now());
//            orderStatusHistoryMapper.insertStatusHistory(history);
//
//            int updated = orderItemMapper.updateOrderByUser(dto);
//            if (updated <= 0) {
//                throw new RuntimeException("No changes applied.");
//            }
//
//            return "Order updated successfully.";
//        } catch (DataUpdateException e) {
//            throw new DataUpdateException("Data cannot be  Update");
//
//        }
//    }
//
//    @Override
//    public Order getOrderById(int userId) {
//        return null;
//    }
//
//    @Override
//    public List<Order> getAllOrders() {
//        return List.of();
//    }
//
//}
