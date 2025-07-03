package com.project.E_Commerce.Repository;

import com.project.E_Commerce.Entity.Product;
import com.project.E_Commerce.Entity.SearchHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface SearchHistoryRepo extends JpaRepository<SearchHistory,Integer> {
    //Get all search history
    @Select("select s from SearchHistory s")
    List<SearchHistory> findAll();

    //Get history by user ID
    @Query("""
    SELECT DISTINCT s.product
    FROM SearchHistory s 
    JOIN s.product p 
    JOIN FETCH p.brand 
    WHERE s.user.id = :userId
""")
    List<Product> findProductsSearchedByUser(@Param("userId") Integer userId);


    //Get history by session ID
    List<SearchHistory> findBySessionId(String sessionId);

    // Get recent N searches for user with pagination
    @Query("SELECT s FROM SearchHistory s WHERE s.userId = :userId ORDER BY s.searchedAt DESC")
    List<SearchHistory> findRecentSearchesByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT DISTINCT s.keyword FROM SearchHistory s WHERE s.user.id = :userId ORDER BY s.searchedAt DESC")
    List<String> getRecentSearchKeywords(@Param("userId") Integer userId);


    // Delete by user ID
    @Query("delete s from SearchHistory s where s.user.userId=: userId")
    int  deleteByUserId(@Param("userId") Integer userId);


    void deleteBySessionId(String sessionId);

    @Query("SELECT sh FROM SearchHistory sh WHERE sh.user.id = :userId ORDER BY sh.searchedAt DESC")
    List<SearchHistory> findByUserIdOrderBySearchedAtDesc(@Param("userId") Integer userId);

}
