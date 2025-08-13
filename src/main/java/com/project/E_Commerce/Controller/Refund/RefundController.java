package com.project.E_Commerce.Controller.Refund;


import com.project.E_Commerce.Service.Return.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refund")
public class RefundController {
    @Autowired
    private RefundService refundService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{returnRequestId}")
    public ResponseEntity<?> processRefund(@PathVariable Integer returnRequestId) {
        String message = refundService.processRefund(returnRequestId);
        return ResponseEntity.ok(message);
    }
}
