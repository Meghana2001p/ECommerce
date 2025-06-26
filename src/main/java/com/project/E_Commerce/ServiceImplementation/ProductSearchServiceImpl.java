package com.project.E_Commerce.ServiceImplementation;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Exception.DataRetrievalException;
import com.project.E_Commerce.Exception.InvalidSearchCriteriaException;
import com.project.E_Commerce.Exception.ProductSearchException;
import com.project.E_Commerce.Mapper.ProductSearchMapper;
import com.project.E_Commerce.Service.ProductSearchService;
import com.project.E_Commerce.Service.ProductService;
import com.project.E_Commerce.dto.ProductFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductSearchMapper productSearchMapper;


    @Override
    public List<Product> getFilteredProducts(ProductFilterRequest filter) {
        try {
           List<Product>  products=  productSearchMapper.getFilteredProducts(filter);
            if (products == null)
            {
                throw new DataRetrievalException("Unexpected null result from product search");
            }
            return products;
        }
        catch (DataAccessException e)
        {
            throw new ProductSearchException("Failed to search products due to database error");

        }


    }

    @Override
    public void validateFilterParameters(ProductFilterRequest filter)
    {

        // Validate price range
        if (filter.getMinPrice() != null && filter.getMaxPrice() != null
                && filter.getMinPrice() > filter.getMaxPrice()) {
            throw new InvalidSearchCriteriaException("Minimum price cannot be greater than maximum price");
        }

        // Validate pagination parameters
        if (filter.getPage() != null && filter.getPage() < 0) {
            throw new InvalidSearchCriteriaException("Page number cannot be negative");
        }

        if (filter.getSize() != null && filter.getSize() <= 0) {
            throw new InvalidSearchCriteriaException("Page size must be positive");
        }
    }
}
