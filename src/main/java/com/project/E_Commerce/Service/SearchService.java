package com.project.E_Commerce.Service;

import com.project.E_Commerce.dto.SearchRequest;
import com.project.E_Commerce.dto.SearchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {
    List<SearchResponse> searchProducts(SearchRequest request);
}
