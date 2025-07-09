package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.DataBaseException;
import com.project.E_Commerce.Exception.NotFoundException;
import com.project.E_Commerce.Exception.OrderNotFoundException;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.PaymentService;
import com.project.E_Commerce.dto.DiscountDto;
import com.project.E_Commerce.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//Payment, Refund, PriceHistory, Discount, ProductDiscount


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
private PaymentRepo paymentRepo;

@Autowired
private RefundRepo refundRepo;

@Autowired
private PriceHistoryRepo priceHistoryRepo;

@Autowired
private DiscountRepo discountRepo;

@Autowired
private ProductDiscountRepo productDiscountRepo;

@Autowired
private OrderRepo orderRepo;

@Autowired
private ProductRepo productRepo;



    @Override
    public Payment makePayment(PaymentRequestDto dto) {
        if(dto==null)
        {
            throw  new IllegalArgumentException("The payment request should not be null");
        }
        try
        {
            Order order = orderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            Optional<Payment> optionalPayment = paymentRepo.findByOrderId(dto.getOrderId());
            Payment payment = optionalPayment.orElseGet(Payment::new);
            payment.setOrder(order);
            payment.setPaymentMethod(dto.getMethod());
            payment.setAmount(dto.getAmount());
            payment.setPaidAt(LocalDateTime.now());
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());

            return paymentRepo.save(payment);
        }
        catch (DataAccessException e) {
            logger.error("Database error while making the payment: {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while  doing the payment ", e);
            throw e;
        }

    }

    @Override
    public Payment getPaymentByOrderId(Integer orderId) {
        if(orderId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }
        try {
            return paymentRepo.findByOrderId(orderId)
                    .orElseThrow(() -> new NotFoundException("Payment not found"));
        }
        catch (DataAccessException e) {
            logger.error("Database error while retrieving the Payment by id : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while  retrieving the Payment by id  ", e);
            throw e;
        }

    }

    @Override
    public List<Payment> getPaymentsByUserId(Integer userId) {
        if(userId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }

        try {
            return paymentRepo.findAllByOrderUserId(userId);
        }
        catch (DataAccessException e) {
            logger.error("Database error while retrieving the Payments done by the User : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while  retrieving the Payments done by the User  ", e);
            throw e;
        }
    }

    @Override
    public void updatePaymentStatus(Integer paymentId, Payment.PaymentStatus status) {
        if(paymentId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }
        try {
            Payment payment = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new NotFoundException("Payment not found"));
            payment.setStatus(status);
            paymentRepo.save(payment);
        }catch (DataAccessException e) {
            logger.error("Database error while  updating the Payments  by the User : {}", e.getMessage(), e); // logs full stack trace

            throw new DataBaseException("Internal server error");
        }
        catch (Exception e) {
            log.error("Unexpected error while  updating the Payments  by the User  ", e);
            throw e;
        }

    }


    //Refund
    @Override
    public Refund initiateRefund(Integer orderId) {

        Payment payment = paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setAmount(payment.getAmount());
        refund.setStatus(Refund.RefundStatus.INITIATED);
        refund.setRefundedAt(LocalDateTime.now());

        return refundRepo.save(refund);





    }

    @Override
    public Refund getRefundById(Integer refundId) {
        return refundRepo.findById(refundId)
                .orElseThrow(() -> new NotFoundException("Refund not found"));
    }

    @Override
    public List<Refund> getRefundsByUserId(Integer userId) {
        return refundRepo.findAllByPaymentOrderUserId(userId);
    }

    @Override
    public void updateRefundStatus(Integer refundId, Refund.RefundStatus status) {
        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() -> new NotFoundException("Refund not found"));
        refund.setStatus(status);
        refundRepo.save(refund);
    }

    //Price History
    @Override
    public void recordPriceChange(Integer productId, Double newPrice) {

    }

    @Override
    public List<PriceHistory> getPriceHistory(Integer productId) {
        return List.of();
    }

    @Override
    public Double getPriceAtDate(Integer productId, LocalDateTime date) {
        return 0.0;
    }

    @Override
    public Discount createDiscount(DiscountDto dto) {
        return null;
    }

    @Override
    public Discount getDiscountByCode(String code) {
        return null;
    }

    @Override
    public List<Discount> getAllActiveDiscounts() {
        return List.of();
    }

    @Override
    public void expireDiscount(Integer id) {

    }

    @Override
    public boolean validateDiscount(String code, Integer userId) {
        return false;
    }

    @Override
    public void assignDiscountToProduct(Integer productId, Integer discountId) {

    }

    @Override
    public ProductDiscount getDiscountForProduct(Integer productId) {
        return null;
    }

    @Override
    public void removeDiscountFromProduct(Integer productId) {

    }
}
