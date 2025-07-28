package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.*;

import java.util.List;

//OrderService
//Order, OrderItem, OrderStatusHistory, DeliveryStatus, GiftOrder
public interface OrderService {

    //the services that can be provided based on the order


    String placeOrder(OrderRequest orderRequest);

    String updateDeliveryStatusByAgent(DeliveryStatusAgentUpdateDto dto);


    void updateDeliveryStatusByAdmin(DeliveryStatusAdminUpdateDto dto);

  String  updateOrderByUser(UserOrderUpdateDto dto);


 Order  getOrderById(int userId);
 //    Order getOrderById(int orderId);
  List<Order> getAllOrders();

}
