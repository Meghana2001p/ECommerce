package com.project.E_Commerce.Controller.Return;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Return.ReturnRequest;
import com.project.E_Commerce.Repository.Order.OrderRepo;
import com.project.E_Commerce.Repository.Return.ReturnRequestRepo;
import com.project.E_Commerce.Service.Return.ReturnService;
import com.project.E_Commerce.UserDetails.UserDetailsImpl;
import com.project.E_Commerce.dto.Return.ReturnRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/return")
public class ReturnController {

    @Autowired
    private ReturnService returnRefundService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ReturnRequestRepo returnRequestRepo;

   @PostMapping("/")
    public ResponseEntity<?> returnRequest(@RequestBody ReturnRequestDTO request)
    {
  Order order= orderRepo.findById(request.getOrderId()).orElseThrow(()-> new RuntimeException("Order doesnot exists"));

  if(order.getUser().getId()!=getCurrentUserId())
  {

      throw new RuntimeException("User cannot access this");
  }
      String message = returnRefundService.requestReturn(request.getOrderId(), request.getReason());
      return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/approve/{orderId}")
    public ResponseEntity<?> approveReturn(@PathVariable Integer returnId) {
        ReturnRequest returnRequest = returnRequestRepo.findById(returnId)
                .orElseThrow(() -> new RuntimeException("Return request not found"));

        String message = returnRefundService.approveReturnByDeliveryPerson(returnId);

        return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/pickedup/{returnId}")
    public ResponseEntity<?> markPickedUp(@PathVariable Integer returnId) {
        ReturnRequest returnRequest = returnRequestRepo.findById(returnId)
                .orElseThrow(() -> new RuntimeException("Return request not found"));

        String message = returnRefundService.markPickedUp(returnId);

        return ResponseEntity.ok(message);
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
