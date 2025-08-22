package com.project.E_Commerce.Service.Order;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.dto.Order.OrderRequest;

import java.util.List;


public interface OrderService
{
    String placeOrder(OrderRequest orderRequest);

    String updateDeliveryStatusByAgent(Integer orderId, Order.OrderStatus newStatus);

    String cancelOrderByUser(Integer orderId, Integer userId);

    Order getOrderById(int userId);

    List<Order> getAllOrders();

}
