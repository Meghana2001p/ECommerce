package com.project.E_Commerce.Controller.Order;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Service.Order.OrderService;
import com.project.E_Commerce.dto.Order.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;



    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        String response = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(response);
    }

     //By deliveryAgent
    @PutMapping("/status/{orderId}")
    public ResponseEntity<String> updateDeliveryStatusByAgent(@PathVariable Integer orderId,@RequestParam Order.OrderStatus newStatus) {
      String message=  orderService.updateDeliveryStatusByAgent(orderId, newStatus);
        return ResponseEntity.ok(message);
        //http://localhost:9090/order/status/5?newStatus=DELIVERED
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrderByUser( @PathVariable Integer orderId, @RequestParam Integer userId) {
        String response = orderService.cancelOrderByUser(orderId, userId);
        return ResponseEntity.ok(response);
        //http://localhost:9090/order/cancel/4?userId=2
    }






}
