package com.project.E_Commerce.Entity.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.E_Commerce.Entity.Product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Integer searchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotBlank(message = "Keyword must not be blank")
    @Size(max = 255, message = "Keyword must be under 255 characters")
    @Column(nullable = false, length = 255)
    private String keyword;


    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt = LocalDateTime.now();
}
