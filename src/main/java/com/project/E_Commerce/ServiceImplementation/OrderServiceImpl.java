//package com.project.E_Commerce.ServiceImplementation;
//
//import com.project.E_Commerce.Entity.*;
//import com.project.E_Commerce.Exception.*;
//import com.project.E_Commerce.Mapper.*;
//import com.project.E_Commerce.Service.CartService;
//import com.project.E_Commerce.Service.OrderService;
//import com.project.E_Commerce.dto.*;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//
//public class OrderServiceImpl implements OrderService {
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//
//    @Autowired
//    private DeliveryStatusMapper deliveryStatusMapper;
//
//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private CartMapper cartMapper;
//    @Autowired
//    private CartItemMapper cartItemMapper;
//
//    @Autowired
//    private AppliedCouponMapper appliedCouponMapper;
//
//
//    @Autowired
//    private OrderItemMapper orderItemMapper;
//
//    @Autowired
//    private GiftOrderMapper giftOrderMapper;
//    @Autowired
//    private PaymentMapper paymentMapper;
//
//
//    @Autowired
//    private NotificationQueueMapper notificationMapper;
//
//    @Autowired
//    private OrderStatusHistoryMapper orderStatusHistoryMapper;
//
//
//    //the user is gonna place the order and the data is gonna be stored in the order table
//    //and also  the response is gonna be there
//    //and the data is gonna be stored in the database of the delivery status
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
