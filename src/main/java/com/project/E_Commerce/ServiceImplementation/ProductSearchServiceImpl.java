package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.ProductAttributeValue;
import com.project.E_Commerce.Exception.DataBaseException;
import com.project.E_Commerce.Exception.DataRetrievalException;
import com.project.E_Commerce.Repository.ProductRepo;
import com.project.E_Commerce.Service.ProductSearchService;
import com.project.E_Commerce.dto.ProductFilterRequest;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
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
        try {
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
                throw new DataRetrievalException("Unexpected null result from product search");
            }

            return pageResult.getContent();

        } catch (DataAccessException e) {
            throw new DataBaseException("Failed to search products due to database error");
        } catch (Exception e) {
        logger.error("Unexpected error had occured while searching ");
            throw e ;
        }

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



//this is used to build the dynamic sql query
//in which we have the Specifications API that cna be used to send the dynamic query
//public class EmployeeService {
//
//    private final EmployeeRepository employeeRepository;
//
//    public List<Employee> searchEmployees(String name, String department, Double minSalary) {
//        return employeeRepository.findAll(
//            Specification.where(EmployeeSpecification.hasName(name))
//                         .and(EmployeeSpecification.hasDepartment(department))
//                         .and(EmployeeSpecification.hasSalaryGreaterThan(minSalary))
//        );
//    }

//    (root, query, cb) ->
//            🔍 What These Variables Represent
//    This is a lambda expression that represents a part of a JPA query builder using the Criteria API.
//
//             1. root
//    Represents the table or entity in the query.
//
//    Example: FROM employee e → root is e.
//
//    So root.get("department") means column department from employee.
//
//             2. query
//    Represents the entire SQL query being built (like SELECT ... WHERE ...).
//
//    Often used for advanced things like DISTINCT, ORDER BY, etc.
//
//    You can ignore it in basic cases.
//
//            3. cb (short for criteriaBuilder)
//    It's a tool to build WHERE conditions (Predicates).
//
//    Used like: cb.equal(...), cb.greaterThan(...), cb.like(...), etc.
//
//            (root, query, cb) -> cb.equal(root.get("department"), "HR")
//            🔁 Translates to SQL:
//    sql
//            Copy
//    Edit
//    SELECT * FROM employee WHERE department = 'HR';
//✅ Another One:
//    java
//            Copy
//    Edit
//            (root, query, cb) -> cb.greaterThan(root.get("salary"), 50000)
//


//here the Spcification creates a dynamic API about the query and this where will help us to find the value if it's not null