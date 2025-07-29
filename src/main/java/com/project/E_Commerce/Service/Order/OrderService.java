package com.project.E_Commerce.Service.Order;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.dto.Order.OrderRequest;

import java.util.List;

//OrderService
//Order, OrderItem, OrderStatusHistory, DeliveryStatus, GiftOrder
public interface OrderService {

    //the services that can be provided based on the order


    String placeOrder(OrderRequest orderRequest);
    String updateDeliveryStatusByAgent(Integer orderId, Order.OrderStatus newStatus) ;
   String cancelOrderByUser(Integer orderId,Integer userId);



        Order  getOrderById(int userId);
   List<Order> getAllOrders();

}
