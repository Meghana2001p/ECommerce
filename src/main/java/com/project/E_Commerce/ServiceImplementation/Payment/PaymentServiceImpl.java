package com.project.E_Commerce.ServiceImplementation.Payment;

import com.project.E_Commerce.Entity.Cart.Discount;
import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.ProductDiscount;
import com.project.E_Commerce.Mapper.DiscountMapper;
import com.project.E_Commerce.Repository.Product.DiscountRepo;
import com.project.E_Commerce.Repository.Product.ProductDiscountRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Service.Payment.PaymentService;
import com.project.E_Commerce.dto.Product.DiscountRequest;
import com.project.E_Commerce.dto.Product.ProductDiscountRequest;
import com.project.E_Commerce.dto.Product.ProductDiscountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    private DiscountRepo discountRepo;

    @Autowired
    private ProductDiscountRepo productDiscountRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private DiscountMapper discountMapper;

    //Discount
    @Override
    public Discount createDiscount(DiscountRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Discount details  must not be null");
        }
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