package com.project.E_Commerce.ServiceImplementation.Payment;

import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Payment.Payment;
import com.project.E_Commerce.Entity.Payment.PriceHistory;
import com.project.E_Commerce.Entity.Payment.Refund;
import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.ProductDiscount;
import com.project.E_Commerce.Mapper.DiscountMapper;
import com.project.E_Commerce.Repository.Order.OrderRepo;
import com.project.E_Commerce.Repository.Payment.PaymentRepo;
import com.project.E_Commerce.Repository.Product.DiscountRepo;
import com.project.E_Commerce.Repository.Product.PriceHistoryRepo;
import com.project.E_Commerce.Repository.Product.ProductDiscountRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.Return.RefundRepo;
import com.project.E_Commerce.Repository.email.EmailNotificationRepo;
import com.project.E_Commerce.Service.Email.EmailService;
import com.project.E_Commerce.Service.Payment.PaymentService;
import com.project.E_Commerce.dto.Payment.PaymentCreateRequestDto;
import com.project.E_Commerce.dto.Payment.PaymentResponseDto;
import com.project.E_Commerce.dto.Payment.UserPaymentSummaryDto;
import com.project.E_Commerce.dto.Product.DiscountRequest;
import com.project.E_Commerce.dto.Product.PriceHistoryDto;
import com.project.E_Commerce.dto.Product.ProductDiscountRequest;
import com.project.E_Commerce.dto.Product.ProductDiscountResponse;
import com.project.E_Commerce.dto.Return.RefundResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private DiscountMapper discountMapper;



    @Override
    public PaymentResponseDto makePayment(PaymentCreateRequestDto requestDto) {
        if(requestDto==null)//has orderID,method,amount
        {
            throw  new IllegalArgumentException("The payment request should not be null");
        }

        // Fetch order
        Order order = orderRepo.findById(requestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: "));

        // Check if payment already exists for the order
        Optional<Payment> optionalPayment = paymentRepo.findByOrderId(requestDto.getOrderId());

        Payment payment = optionalPayment.orElseGet(Payment::new);
        //Payment payment = paymentRepo.findByOrderId(requestDto.getOrderId()).orElse(new Payment());

        payment.setOrder(order);
        //  payment.setPaymentMethod(requestDto.getMethod());
        payment.setAmount(requestDto.getAmount());
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        Payment saved = paymentRepo.save(payment);



        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        //  dto.setMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaidAt(payment.getPaidAt());
        dto.setAmount(payment.getAmount());
        return dto;



    }



    @Override
    public PaymentResponseDto getPaymentByOrderId(Integer orderId) {
        if(orderId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }

        paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        PaymentResponseDto response = new PaymentResponseDto();
        Optional<Payment> payment = paymentRepo.findByOrderId(orderId);

        Payment paymentexisting = payment.get();

        response.setOrderId(paymentexisting.getOrder().getId());
        // response.setMethod(paymentexisting.getPaymentMethod());
        response.setStatus(paymentexisting.getStatus());
        response.setTransactionId(paymentexisting.getTransactionId());
        response.setPaidAt(paymentexisting.getPaidAt());
        response.setAmount(paymentexisting.getAmount());

        return response;



    }

    @Override
    public List<UserPaymentSummaryDto> getPaymentsByUserId(Integer userId) {
        if(userId<=0)
        {
            throw new IllegalArgumentException("In valid order id");
        }


        return null;


    }

    @Override
    public void updatePaymentStatus(Integer paymentId, Payment.PaymentStatus status) {
        if (paymentId == null || paymentId <= 0) {
            throw new IllegalArgumentException("Invalid payment ID: " + paymentId);
        }

        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }


        Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (status.equals(payment.getStatus())) {
            log.info("Payment ID {} already has status {}", paymentId, status);
            return;
        }
        payment.setStatus(status);
        paymentRepo.save(payment);




    }



    //Price History
    @Override
    public void recordPriceChange(Integer productId, Double newPrice) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

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

        List<PriceHistory> historyList = priceHistoryRepo.findByProductIdOrderByChangedAtDesc(productId);

        return historyList.stream()
                .map(p -> new PriceHistoryDto(
                        p.getOldPrice(),
                        p.getNewPrice(),
                        p.getChangedAt()
                ))
                .collect(Collectors.toList());


    }


    @Override
    public Double getPriceAtDate(Integer productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return product.getPrice().doubleValue();

    }


    //Discount



    @Override
    public Discount createDiscount(DiscountRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Discount details  must not be null");
        }
// Check for duplicate discount name (case-insensitive)
        Optional<Discount> existingDiscount = discountRepo.findByNameIgnoreCase(dto.getName());
        if (existingDiscount.isPresent()) {
            throw new IllegalArgumentException("A discount with the same name already exists.");
        }

        Discount discount= discountMapper.toEntity(dto);

        return discountRepo.save(discount);


    }


    @Override
    public List<Discount> getAllActiveDiscounts() {

        return discountRepo.findAllByIsActiveTrue();

    }

    @Override
    public void deleteDiscount(Integer id) {

        if(id == null || id<=0)
        {
            throw new IllegalArgumentException("Invalid input");
        }
        discountRepo.deleteById(id);

    }





    //Product Discount

    @Override
    public void assignDiscountToProduct(ProductDiscountRequest request) {
        if(request==null)
        {
            throw new IllegalArgumentException("Request cannot be null");
        }
        int discountId=  request.getDiscountId();
        int  productId= request.getProductId();

        if(discountId<=0||productId<=0)
        {
            throw  new IllegalArgumentException("Data are invalid");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        Discount discount = discountRepo.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with ID: " + discountId));

        ProductDiscount productDiscount = new ProductDiscount();


        productDiscount.setProduct(product);
        if( discount.getIsActive())
        {
            productDiscount.setDiscount(discount);
        }

        productDiscountRepo.save(productDiscount);


    }
    public ProductDiscountResponse getDiscountForProduct(Integer productId) {
        ProductDiscount pd = productDiscountRepo.findByProductId(productId);
        if (pd == null) {
            throw new IllegalArgumentException("No discount found for product ID: " + productId);
        }

        Discount d = pd.getDiscount();

        ProductDiscountResponse response = new ProductDiscountResponse();
        response.setProductId(productId);
        response.setDiscountName(d.getName());
        response.setDiscountPercent(d.getDiscountPercent());
        response.setStartDate(d.getStartDate());
        response.setEndDate(d.getEndDate());

        return response;
    }

    @Override
    public void removeDiscountFromProduct(Integer productId) {

        if (!productRepo.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        productDiscountRepo.deleteByProductId(productId);


    }




}