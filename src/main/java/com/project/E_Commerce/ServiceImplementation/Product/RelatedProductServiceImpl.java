package com.project.E_Commerce.ServiceImplementation.Product;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.RelatedProduct;
import com.project.E_Commerce.Repository.Product.ProductDiscountRepo;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Repository.Product.ReviewRepo;
import com.project.E_Commerce.Repository.Return.RelatedProductRepo;
import com.project.E_Commerce.Service.Product.RelatedProductService;
import com.project.E_Commerce.dto.Product.RelatedProductRequest;
import com.project.E_Commerce.dto.Product.RelatedProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RelatedProductServiceImpl implements RelatedProductService {
    @Autowired
    private RelatedProductRepo relatedProductRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ReviewRepo reviewRepo;

@Autowired
private ProductDiscountRepo productDiscountRepo;

    @Override
    public void addRelatedProduct(RelatedProductRequest request) {
        Optional<RelatedProduct> existing = relatedProductRepo.findByProductIdAndRelatedProductIdAndRelationshipType(
                request.getProductId(), request.getRelatedProductId(),request.getRelationshipType());

        if (existing.isPresent()) {
            throw new RuntimeException("Relation already exists");
        }

        Product mainProduct = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Main product not found"));

        Product related = productRepo.findById(request.getRelatedProductId())
                .orElseThrow(() -> new RuntimeException("Related product not found"));

        RelatedProduct relation = new RelatedProduct();
        relation.setProduct(mainProduct);
        relation.setRelatedProduct(related);
        relation.setRelationshipType(request.getRelationshipType());

        relatedProductRepo.save(relation);
    }

    @Override
    public List<RelatedProductResponse> getRelatedProductsByProductId(Integer productId) {
        List<RelatedProduct> relatedList = relatedProductRepo.findByProductId(productId);

        return relatedList.stream().map(r -> {
            Product p = r.getRelatedProduct();

            BigDecimal discountPercent = getActiveDiscountPercent(p.getId());
            BigDecimal discountedPrice = applyDiscount(p.getPrice(), discountPercent);
            Double avgRating = reviewRepo.findAverageRatingByProductId(p.getId());

            RelatedProductResponse response = new RelatedProductResponse();
            response.setProductId(p.getId());
            response.setName(p.getName());
            response.setImageAdress(p.getImageAddress());
            response.setDiscountPercent(discountedPrice);
            response.setOriginalPrice(p.getPrice());
            response.setDiscountPercent(discountPercent);
            response.setAverageRating(avgRating);
            response.setIsAvailable(p.getIsAvailable());
            response.setRelationshipType(r.getRelationshipType());
            response.setAverageRating(null);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public String removeRelatedProduct( Integer relatedProductId) {
        relatedProductRepo.deleteById(relatedProductId);
        return "Product deleted successfully";

    }

    private BigDecimal getActiveDiscountPercent(Integer productId) {
        Optional<BigDecimal> discounts = productDiscountRepo.findDiscountPercentByProductId(productId);
        if (discounts.isEmpty()) return null;
        return  discounts.get();
    }

    private BigDecimal applyDiscount(BigDecimal price, BigDecimal percent) {
        if (percent == null) return price;
        return price.subtract(price.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }

}
