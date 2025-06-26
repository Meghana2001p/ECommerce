package com.project.E_Commerce.Mapper;

import com.project.E_Commerce.Entity.SearchHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SearchHistoryMapper {

    // Insert new search record
    @Insert("""
        INSERT INTO search_history (user_id, product_id, keyword, session_id, searched_at)
        VALUES (#{userId}, #{productId}, #{keyword}, #{sessionId}, #{searchedAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "searchId")
    int createSearchHistory(SearchHistory searchHistory);

    // Get all search history
    @Select("SELECT * FROM search_history")
    List<SearchHistory> getAllSearchHistory();

    // Get history by user ID
    @Select("SELECT * FROM search_history WHERE user_id = #{userId}")
    List<SearchHistory> getSearchHistoryByUserId(@Param("userId") int userId);

    // Get history by session ID
    @Select("SELECT * FROM search_history WHERE session_id = #{sessionId}")
    List<SearchHistory> getSearchHistoryBySessionId(@Param("sessionId") String sessionId);

    // Get recent N searches for user
    @Select("""
        SELECT * FROM search_history
        WHERE user_id = #{userId}
        ORDER BY searched_at DESC
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<SearchHistory> getRecentSearchesForUser( @Param("userId") int userId,
                                                  @Param("limit") int limit,
                                                  @Param("offset") int offset);




    @Delete("delete from search_history where user_id=#{userId}")
   int  deleteSearchHistoryById(@Param("userId") int userId);


    @Delete("delete from search_history where session_id=#{sessionId}")
    int  deleteSearchHistoryBySessionId(@Param("userId") String  sessionId);
}
