package com.project.E_Commerce.Controller.Order;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Service.Order.OrderService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import com.project.E_Commerce.dto.Order.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;


   @PreAuthorize("hasRole('USER')")
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
       if(orderRequest.getUserId()!=getCurrentUserId())
       {
           throw new RuntimeException("Current User Cannot place the order");
       }
        String response = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(response);
    }


     @PreAuthorize("hasRole('DELIVERY')")
     @PutMapping("/status/{orderId}/{newStatus}")
    public ResponseEntity<String> updateDeliveryStatusByAgent(@PathVariable Integer orderId,@RequestParam Order.OrderStatus newStatus) {

      String message=  orderService.updateDeliveryStatusByAgent(orderId, newStatus);
        return ResponseEntity.ok(message);
        //http://localhost:9090/order/status/5?newStatus=DELIVERED
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cancel/{orderId}/{userId}")
    public ResponseEntity<String> cancelOrderByUser( @PathVariable Integer orderId, @PathVariable Integer userId)
    {
        if(userId!=getCurrentUserId())
        {
            throw new RuntimeException("Current User Cannot place the order");
        }
        String response = orderService.cancelOrderByUser(orderId, userId);
        return ResponseEntity.ok(response);
        //http://localhost:9090/order/cancel/4?userId=2
    }


    public int getCurrentUserId()
    {
        //get the authentication object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl)
        {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            return userDetails.getId();

        }
        throw new RuntimeException("Unauthorized access");
    }




}
