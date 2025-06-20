package com.project.E_Commerce.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistory {

    private Integer searchId;

    private Integer userId;

    private Integer productId;

    @NotBlank(message = "Keyword must not be blank")
    @Size(max = 255, message = "Keyword must be under 255 characters")
    private String keyword;

    @Size(max = 255, message = "Session ID too long")
    private String sessionId;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchedAt = LocalDateTime.now();
}
