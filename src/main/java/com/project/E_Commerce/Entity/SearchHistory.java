package com.project.E_Commerce.Entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistory {

    private int searchId;
    private int userId;
    private int productId;
    private String keyword;
    private String sessionId;  // For guest tracking

    @Column(updatable = false)
    private LocalDateTime searchedAt= LocalDateTime.now();


}
