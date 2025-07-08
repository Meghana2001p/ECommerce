package com.project.E_Commerce.Controller;

import com.project.E_Commerce.Entity.Order;
import com.project.E_Commerce.Service.OrderService;
import com.project.E_Commerce.dto.DeliveryStatusAdminUpdateDto;
import com.project.E_Commerce.dto.DeliveryStatusAgentUpdateDto;
import com.project.E_Commerce.dto.OrderPlacedResponseDto;
import com.project.E_Commerce.dto.UserOrderUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    @Autowired
    private  OrderService orderService;


    @PostMapping("/place")
    public ResponseEntity<OrderPlacedResponseDto> placeOrder(@RequestBody Order orderRequest) {
        OrderPlacedResponseDto response = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/delivery/agent")
    public ResponseEntity<String> updateDeliveryStatusByAgent(@RequestBody DeliveryStatusAgentUpdateDto dto) {
        String result = orderService.updateDeliveryStatusByAgent(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/delivery/admin")
    public ResponseEntity<String> updateDeliveryStatusByAdmin(@RequestBody DeliveryStatusAdminUpdateDto dto) {
        orderService.updateDeliveryStatusByAdmin(dto);
        return ResponseEntity.ok("Delivery status updated successfully by admin.");
    }

    @PutMapping("/user/update")
    public ResponseEntity<String> updateOrderByUser(@RequestBody UserOrderUpdateDto dto) {
        String result = orderService.updateOrderByUser(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
