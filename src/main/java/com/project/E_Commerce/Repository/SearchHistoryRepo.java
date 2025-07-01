package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepo extends JpaRepository<SearchHistory,Integer> {
}
