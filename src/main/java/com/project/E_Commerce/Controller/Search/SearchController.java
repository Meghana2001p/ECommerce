package com.project.E_Commerce.Controller.Search;

import com.project.E_Commerce.Service.User.SearchService;
import com.project.E_Commerce.dto.User.SearchRequest;
import com.project.E_Commerce.dto.User.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/search")
public class SearchController {

    @Autowired
    private SearchService productSearchService;

    @GetMapping
    public ResponseEntity<List<?>> searchProducts(@RequestBody SearchRequest request) {
        List<SearchResponse> results = productSearchService.searchProducts(request);
        return ResponseEntity.ok(results);
    }
}
