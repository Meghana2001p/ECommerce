package com.project.E_Commerce.ServiceImplementation.User;


import com.project.E_Commerce.Repository.Product.ProductRepo;
import com.project.E_Commerce.Service.User.SearchService;
import com.project.E_Commerce.dto.User.SearchRequest;
import com.project.E_Commerce.dto.User.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductRepo productRepository;

    @Override
    public List<SearchResponse> searchProducts(SearchRequest request) {
        String keyword = request.getKeyword();
        String brand = request.getBrand();
        String color = request.getColor();

        // Extract sizes as List<String>
        List<String> sizes = request.getSizes();
        int sizesLength = (sizes != null) ? sizes.size() : 0;

        // Convert comma-separated categories to List<String>
        List<String> categories = null;
        int categoriesLength = 0;
        if (request.getCategories() != null && !request.getCategories().trim().isEmpty()) {
            categories = Arrays.stream(request.getCategories().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            categoriesLength = categories.size();
        }

        Double minPrice = request.getMinPrice();
        Double maxPrice = request.getMaxPrice();
        Integer ratings = request.getRatings();
        String sortBy = request.getSortBy(); // optional if not used in native SQL

        int page = (request.getPage() != null && request.getPage() > 0) ? request.getPage() : 1;
        int limit = 5;
        int offset = (page - 1) * limit;

        return productRepository.searchProducts(
                keyword,
                brand,
                color,
                sizes,
                sizesLength,
                categories,
                categoriesLength,
                minPrice,
                maxPrice,
                ratings,
                limit,
                offset
        );
    }

}
