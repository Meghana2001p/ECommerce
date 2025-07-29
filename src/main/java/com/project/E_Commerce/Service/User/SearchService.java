package com.project.E_Commerce.Service.User;

import com.project.E_Commerce.dto.User.SearchRequest;
import com.project.E_Commerce.dto.User.SearchResponse;

import java.util.List;

public interface SearchService {
    List<SearchResponse> searchProducts(SearchRequest request);
}
