package com.project.E_Commerce.ServiceImplementation.Refund;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Payment.Refund;
import com.project.E_Commerce.Entity.Payment.Transaction;
import com.project.E_Commerce.Entity.Return.ReturnRequest;
import com.project.E_Commerce.Repository.Order.OrderRepo;
import com.project.E_Commerce.Repository.Payment.TransactionRepo;
import com.project.E_Commerce.Repository.Return.RefundRepo;
import com.project.E_Commerce.Repository.Return.ReturnRequestRepo;
import com.project.E_Commerce.Service.Return.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RefundServiceImpl implements RefundService {


    @Autowired
    private ReturnRequestRepo returnRequestRepo;

    @Autowired
    private RefundRepo refundRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private OrderRepo orderRepo;


        public String processRefund(Integer returnRequestId) {
            ReturnRequest returnRequest = returnRequestRepo.findById(returnRequestId)
                    .orElseThrow(() -> new RuntimeException("Return request not found"));

            if (returnRequest.getStatus() != ReturnRequest.ReturnStatus.PICKED_UP) {
                return "Refund cannot be processed. Return request status: " + returnRequest.getStatus();
            }

            Order order = returnRequest.getOrderItem();

            Transaction transaction = transactionRepo.findByOrderId(order.getId())
                    .orElseThrow(() -> new RuntimeException("No payment found for this order"));

            BigDecimal refundAmount = transaction.getAmount();

            Refund refund = new Refund();
            refund.setOrder(order);
            refund.setAmount(refundAmount);
            refund.setStatus(Refund.RefundStatus.COMPLETED);
            refund.setUpdatedAt(LocalDateTime.now());
            refundRepo.save(refund);

            returnRequest.setStatus(ReturnRequest.ReturnStatus.COMPLETED);
            returnRequestRepo.save(returnRequest);

            order.setOrderStatus(Order.OrderStatus.RETURNED);
            orderRepo.save(order);
            return "Refund processed successfully for ReturnRequest ID: " + returnRequestId;
        }

    }
