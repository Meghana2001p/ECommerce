package com.project.E_Commerce.Service;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.dto.DeliveryStatusAdminUpdateDto;
import com.project.E_Commerce.dto.DeliveryStatusAgentUpdateDto;
import com.project.E_Commerce.dto.OrderPlacedResponseDto;
import com.project.E_Commerce.dto.UserOrderUpdateDto;

import java.util.List;

//OrderService
//Order, OrderItem, OrderStatusHistory, DeliveryStatus, GiftOrder
public interface OrderService {

    //the services that can be provided based on the order


    OrderPlacedResponseDto placeOrder(Order orderRequest);

    String updateDeliveryStatusByAgent(DeliveryStatusAgentUpdateDto dto);


    void updateDeliveryStatusByAdmin(DeliveryStatusAdminUpdateDto dto);

  String  updateOrderByUser(UserOrderUpdateDto dto);


 Order  getOrderById(int userId);
 //    Order getOrderById(int orderId);
  List<Order> getAllOrders();

}
