package com.project.E_Commerce.ServiceImplementation.User;

import com.project.E_Commerce.Entity.Product.Product;
import com.project.E_Commerce.Entity.Product.ProductAttributeValue;
import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Service.Product.ProductSearchService;
import com.project.E_Commerce.dto.Product.ProductFilterRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);


    @Autowired
    private ProductRepo productRepository;

    @Override
    public List<Product> getFilteredProducts(ProductFilterRequest filter) {
        validateFilterParameters(filter);

            Specification<Product> spec = (root, query, cb) -> {
                List<Predicate> predicates = new java.util.ArrayList<>();

                if (filter.getMinPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
                }

                if (filter.getMaxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
                }

                if (filter.getBrands() != null && !filter.getBrands().isEmpty()) {
                    predicates.add(root.get("brand").get("brandName").in(filter.getBrands()));
                }

                if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                    String keywordPattern = "%" + filter.getKeyword().toLowerCase() + "%";
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("name")), keywordPattern),
                            cb.like(cb.lower(root.get("description")), keywordPattern)
                    ));
                }

                // Colors
                if (filter.getColors() != null && !filter.getColors().isEmpty()) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<ProductAttributeValue> pav = subquery.from(ProductAttributeValue.class);
                    Join<Object, Object> attr = pav.join("attribute");
                    subquery.select(cb.literal(1L))
                            .where(
                                    cb.equal(pav.get("product").get("id"), root.get("id")),
                                    cb.equal(attr.get("name"), "Color"),
                                    pav.get("value").in(filter.getColors())
                            );
                    predicates.add(cb.exists(subquery));
                }

                // Sizes
                if (filter.getSizes() != null && !filter.getSizes().isEmpty()) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<ProductAttributeValue> pav = subquery.from(ProductAttributeValue.class);
                    Join<Object, Object> attr = pav.join("attribute");
                    subquery.select(cb.literal(1L))
                            .where(
                                    cb.equal(pav.get("product").get("id"), root.get("id")),
                                    cb.equal(attr.get("name"), "Size"),
                                    pav.get("value").in(filter.getSizes())
                            );
                    predicates.add(cb.exists(subquery));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            Sort sort = Sort.by(
                    Sort.Direction.fromString(
                            filter.getSortDirection() != null ? filter.getSortDirection() : "ASC"
                    ),
                    filter.getSortBy() != null ? filter.getSortBy() : "id"
            );

            Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

            Page<Product> pageResult = productRepository.findAll(spec , pageable);

            if (pageResult == null) {
                throw new IllegalArgumentException("Unexpected null result from product search");
            }

            return pageResult.getContent();



    }
        @Override
        public void validateFilterParameters (ProductFilterRequest filter){
                if (filter.getPage() < 0) {
                    throw new IllegalArgumentException("Page number must be non-negative");
                }
                if (filter.getSize() <= 0) {
                    throw new IllegalArgumentException("Page size must be greater than 0");
                }

                if (filter.getMinPrice() != null && filter.getMinPrice() < 0) {
                    throw new IllegalArgumentException("Min price cannot be negative");
                }

                if (filter.getSortDirection() != null &&
                        !filter.getSortDirection().equalsIgnoreCase("asc") &&
                        !filter.getSortDirection().equalsIgnoreCase("desc")) {
                    throw new IllegalArgumentException("Sort direction must be 'asc' or 'desc'");
                }

        }

}

