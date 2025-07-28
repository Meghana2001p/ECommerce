package com.project.E_Commerce.Controller.Order;

import com.project.E_Commerce.Service.OrderService;
import com.project.E_Commerce.dto.*;
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



    @PostMapping("/")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
        //OrderPlacedResponseDto response = orderService.placeOrder(orderRequest);
      return ResponseEntity.ok(" ");
    }

}
