package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.RelatedProduct;
import com.project.E_Commerce.Repository.ProductDiscountRepo;
import com.project.E_Commerce.Repository.ProductRepo;
import com.project.E_Commerce.Repository.RelatedProductRepo;
import com.project.E_Commerce.Repository.ReviewRepo;
import com.project.E_Commerce.Service.RelatedProductService;
import com.project.E_Commerce.dto.RelatedProductRequest;
import com.project.E_Commerce.dto.RelatedProductResponse;
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
            response.setThumbnailUrl(p.getImageAddress()); // adjust if you use separate image repo
            response.setPrice(discountedPrice);
            response.setOriginalPrice(p.getPrice());
            response.setDiscountPercent(discountPercent != null ? discountPercent.intValue() : 0);
            response.setAverageRating(avgRating);
            response.setIsAvailable(p.getIsAvailable());
            response.setRelationshipType(r.getRelationshipType());

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
