package com.project.E_Commerce.Service.Return;

import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Payment.Refund;
import com.project.E_Commerce.Entity.Payment.Transaction;
import com.project.E_Commerce.Entity.Return.ReturnRequest;
import com.project.E_Commerce.Repository.Order.OrderRepo;
import com.project.E_Commerce.Repository.Payment.TransactionRepo;
import com.project.E_Commerce.Repository.Return.RefundRepo;
import com.project.E_Commerce.Repository.Return.ReturnRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface RefundService {

     String processRefund(Integer returnRequestId);


}
