package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.*;
import com.project.E_Commerce.Exception.DataBaseException;
import com.project.E_Commerce.Exception.NotFoundException;
import com.project.E_Commerce.Exception.OrderNotFoundException;
import com.project.E_Commerce.Exception.ProductNotFoundException;
import com.project.E_Commerce.Repository.*;
import com.project.E_Commerce.Service.EmailService;
import com.project.E_Commerce.Service.PaymentService;
import com.project.E_Commerce.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

@Autowired
private EmailNotificationRepo emailNotificationRepo;

@Autowired
private EmailService emailService;



    @Override
    public PaymentResponseDto makePayment(PaymentCreateRequestDto requestDto) {
        if(requestDto==null)//has orderID,method,amount
        {
            throw  new IllegalArgumentException("The payment request should not be null");
        }
        try{
            // Fetch order
            Order order = orderRepo.findById(requestDto.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: "));

            // Check if payment already exists for the order
            Optional<Payment> optionalPayment = paymentRepo.findByOrderId(requestDto.getOrderId());

            Payment payment = optionalPayment.orElseGet(Payment::new);
            //Payment payment = paymentRepo.findByOrderId(requestDto.getOrderId()).orElse(new Payment());

            payment.setOrder(order);
            payment.setPaymentMethod(requestDto.getMethod());
            payment.setAmount(requestDto.getAmount());
            payment.setPaidAt(LocalDateTime.now());
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());
            Payment saved = paymentRepo.save(payment);



            PaymentResponseDto dto = new PaymentResponseDto();
            dto.setId(payment.getId());
            dto.setOrderId(payment.getOrder().getId());
            dto.setMethod(payment.getPaymentMethod());
            dto.setStatus(payment.getStatus());
            dto.setTransactionId(payment.getTransactionId());
            dto.setPaidAt(payment.getPaidAt());
            dto.setAmount(payment.getAmount());
            return dto;


        }catch (DataAccessException e) {
            log.error("Database error while making the payment: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            log.error("Unexpected error while doing the payment", e);
            throw e;
        }
    }



    @Override
    public PaymentResponseDto getPaymentByOrderId(Integer orderId) {
        if(orderId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }
        try {
            paymentRepo.findByOrderId(orderId)
                    .orElseThrow(() -> new NotFoundException("Payment not found"));
            PaymentResponseDto response = new PaymentResponseDto();
        Optional<Payment>payment =    paymentRepo.findByOrderId(orderId);

        Payment paymentexisting= payment.get();

  response.setOrderId(paymentexisting.getOrder().getId());
  response.setMethod(paymentexisting.getPaymentMethod());
  response.setStatus(paymentexisting.getStatus());
  response.setTransactionId(paymentexisting.getTransactionId());
  response.setPaidAt(paymentexisting.getPaidAt());
  response.setAmount(paymentexisting.getAmount());

  return response;

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
    public List<UserPaymentSummaryDto> getPaymentsByUserId(Integer userId) {
        if(userId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }

        try {
            return paymentRepo.findAllPaymentSummaryByUserId(userId);
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
        if (paymentId == null || paymentId <= 0) {
            throw new IllegalArgumentException("Invalid payment ID: " + paymentId);
        }

        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }

        try {
            Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> new NotFoundException("Payment not found"));
            if (status.equals(payment.getStatus())) {
                log.info("Payment ID {} already has status {}", paymentId, status);
                return;
            }
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
    public RefundResponseDto initiateRefund(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid order ID");
        }

        try {
            // Fetch payment
            Payment payment = paymentRepo.findByOrderId(orderId)
                    .orElseThrow(() -> new NotFoundException("Payment not found for order ID: " + orderId));

            // Create refund
            Refund refund = new Refund();
            refund.setPayment(payment);
            refund.setAmount(payment.getAmount());
            refund.setStatus(Refund.RefundStatus.INITIATED);
            refund.setRefundedAt(LocalDateTime.now());

            Refund savedRefund = refundRepo.save(refund);

            // Send refund confirmation email
            String userEmail = payment.getOrder().getUser().getEmail(); // Assuming User entity has getEmail()
            String subject = "Refund Initiated - Order #" + orderId;
            String body = "Dear " + payment.getOrder().getUser().getName() + ",\n\n"
                    + "Your refund of ₹" + savedRefund.getAmount() + " has been initiated for Order ID: " + orderId + ".\n"
                    + "Transaction ID: " + payment.getTransactionId() + "\n"
                    + "Refund Status: " + savedRefund.getStatus() + "\n"
                    + "Refund Date: " + savedRefund.getRefundedAt() + "\n\n"
                    + "Thank you for shopping with us.\nE-Commerce Team.";

            emailService.sendEmail(userEmail, subject, body);

            return new RefundResponseDto(
                    savedRefund.getId(),
                    payment.getId(),
                    savedRefund.getAmount(),
                    savedRefund.getStatus().name(),
                    savedRefund.getRefundedAt(),
                    "Refund confirmation email sent to " + userEmail
            );

        } catch (DataAccessException e) {
            logger.error("Database error while initiating refund for order ID {}: {}", orderId, e.getMessage(), e);
            throw new DataBaseException("Internal server error during refund initiation");
        } catch (Exception e) {
            logger.error("Unexpected error while initiating refund for order ID {}: ", orderId, e);
            throw e;
        }
    }


    @Override
    public RefundResponseDto getRefundById(Integer refundId) {
        if (refundId == null || refundId <= 0) {
            throw new IllegalArgumentException("Invalid refund ID");
        }

        try {
            Refund refund = refundRepo.findById(refundId)
                    .orElseThrow(() -> new NotFoundException("Refund not found with ID: " + refundId));

            Payment payment = refund.getPayment();
            Order order = payment.getOrder();

            return new RefundResponseDto(
                    refund.getId(),
                    order.getId(),
                    payment.getId(),
                    refund.getAmount(),
                    refund.getStatus().name(),
                    refund.getRefundedAt(),
                    "Refund retrieved successfully"
            );

        } catch (DataAccessException e) {
            logger.error("Database error while fetching refund with ID {}: {}", refundId, e.getMessage(), e);
            throw new DataBaseException("Internal server error while retrieving refund");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching refund with ID {}: {}", refundId, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public List<RefundResponseDto> getRefundsByUserId(Integer userId) {
        List<Refund> refunds = refundRepo.findAllByPaymentOrderUserId(userId);

        return refunds.stream()
                .map(refund -> {
                    Payment payment = refund.getPayment();
                    Order order = payment.getOrder();

                    return new RefundResponseDto(
                            refund.getId(),
                            order.getId(),
                            payment.getId(),
                            refund.getAmount(),
                            refund.getStatus().name(),
                            refund.getRefundedAt(),
                            "Refund fetched successfully"
                    );
                })
                .collect(Collectors.toList());
    }


    @Override
    public RefundResponseDto updateRefundStatus(Integer refundId, Refund.RefundStatus status) {
        if (refundId == null || refundId <= 0) {
            throw new IllegalArgumentException("Invalid refund ID");
        }

        try {
            Refund refund = refundRepo.findById(refundId)
                    .orElseThrow(() -> new NotFoundException("Refund not found"));

            refund.setStatus(status);
            refund = refundRepo.save(refund);

            Payment payment = refund.getPayment();
            Order order = payment.getOrder();

            return new RefundResponseDto(
                    refund.getId(),
                    order.getId(),
                    payment.getId(),
                    refund.getAmount(),
                    refund.getStatus().name(),
                    refund.getRefundedAt(),
                    "Refund status updated successfully"
            );

        } catch (DataAccessException e) {
            logger.error("Database error while updating refund status: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error");
        } catch (Exception e) {
            logger.error("Unexpected error while updating refund status", e);
            throw e;
        }
    }

    //Price History
    @Override
    public void recordPriceChange(Integer productId, Double newPrice) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        BigDecimal oldPrice = product.getPrice();
        BigDecimal updatedPrice = BigDecimal.valueOf(newPrice);

        if (oldPrice.compareTo(updatedPrice) == 0) {
            log.info("Price did not change for product ID {}", productId);
            return;
        }

        // 1. Update product price
        product.setPrice(updatedPrice);
        productRepo.save(product);

        // 2. Record in PriceHistory
        PriceHistory history = new PriceHistory();
        history.setProduct(product);
        history.setOldPrice(oldPrice);
        history.setNewPrice(updatedPrice);
        history.setChangedAt(LocalDateTime.now());

        priceHistoryRepo.save(history);
    }


    @Override
    public List<PriceHistoryDto> getPriceHistory(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        try {
            List<PriceHistory> historyList = priceHistoryRepo.findByProductIdOrderByChangedAtDesc(productId);

            return historyList.stream()
                    .map(p -> new PriceHistoryDto(
                            p.getOldPrice(),
                            p.getNewPrice(),
                            p.getChangedAt()
                    ))
                    .collect(Collectors.toList());

        } catch (DataAccessException e) {
            log.error("Database error while fetching price history for product ID {}: {}", productId, e.getMessage(), e);
            throw new DataBaseException("Internal server error while retrieving price history");
        } catch (Exception e) {
            log.error("Unexpected error while fetching price history for product ID {}", productId, e);
            throw e;
        }
    }


    @Override
    public Double getPriceAtDate(Integer productId) {
        try {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            return product.getPrice().doubleValue();
        }catch (DataAccessException e) {
            logger.error("Database error while fetching current price for product ID {}: {}", productId, e.getMessage(), e);
            throw new DataBaseException("Internal server error while retrieving product price");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching current price for product ID {}: {}", productId, e);
            throw e;
        }
    }


    //Discount



    @Override
    public Discount createDiscount(DiscountDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Discount DTO must not be null");
        }

        try {
            Discount discount = new Discount();
            discount.setName(dto.getName());
            discount.setDiscountPercent(dto.getDiscountPercent());
            discount.setStartDate(dto.getStartDate());
            discount.setEndDate(dto.getEndDate());
            discount.setIsActive(true);

            return discountRepo.save(discount);

        } catch (DataAccessException e) {
            log.error("Database error while creating discount: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error while creating discount");
        } catch (Exception e) {
            log.error("Unexpected error while creating discount", e);
            throw e;
        }
    }

    @Override
    public Discount getDiscountByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Discount code must not be blank");
        }

        return discountRepo.findByName(code)
                .orElseThrow(() -> new NotFoundException("Discount with code '" + code + "' not found"));
    }

    @Override
    public List<Discount> getAllActiveDiscounts() {
        try {
            return discountRepo.findAllByIsActiveTrue();
        } catch (DataAccessException e) {
            log.error("Database error while fetching active discounts: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error while fetching active discounts");
        }
    }

    @Override
    public void expireDiscount(Integer id) {
        Discount discount = discountRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found with ID: " + id));

        discount.setIsActive(false);
        discountRepo.save(discount);
    }

    @Override
    public boolean validateDiscount(String code, Integer userId) {
        Discount discount = discountRepo.findByName(code)
                .orElseThrow(() -> new NotFoundException("Discount not found with code: " + code));

        LocalDateTime now = LocalDateTime.now();
        return discount.getIsActive() &&
                now.isAfter(discount.getStartDate()) &&
                now.isBefore(discount.getEndDate());
    }




    //Product Discount

    @Override
    public void assignDiscountToProduct(Integer productId, Integer discountId) {
        try {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

            Discount discount = discountRepo.findById(discountId)
                    .orElseThrow(() -> new NotFoundException("Discount not found with ID: " + discountId));

            ProductDiscount productDiscount = new ProductDiscount();
            productDiscount.setProduct(product);
            productDiscount.setDiscount(discount);

            productDiscountRepo.save(productDiscount);

        } catch (DataAccessException e) {
            log.error("Database error while assigning discount: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error while assigning discount to product");
        } catch (Exception e) {
            log.error("Unexpected error while assigning discount to product", e);
            throw e;
        }
    }

    @Override
    public ProductDiscount getDiscountForProduct(Integer productId) {
        try {
            ProductDiscount discount = productDiscountRepo.findByProductId(productId);
            if (discount == null) {
                throw new NotFoundException("No discount found for product ID: " + productId);
            }
            return discount;

        } catch (DataAccessException e) {
            log.error("Database error while fetching discount: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error while retrieving discount for product");
        } catch (Exception e) {
            log.error("Unexpected error while retrieving discount for product", e);
            throw e;
        }
    }

    @Override
    public void removeDiscountFromProduct(Integer productId) {
        try {
            if (!productRepo.existsById(productId)) {
                throw new NotFoundException("Product not found with ID: " + productId);
            }
            productDiscountRepo.deleteByProductId(productId);

        } catch (DataAccessException e) {
            log.error("Database error while removing discount from product: {}", e.getMessage(), e);
            throw new DataBaseException("Internal server error while removing discount from product");
        } catch (Exception e) {
            log.error("Unexpected error while removing discount from product", e);
            throw e;
        }
    }




}
