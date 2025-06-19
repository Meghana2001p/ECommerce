package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {

    private Integer id;
    private Integer orderItemId;

    private String reason;

    private ReturnStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestedAt;


    public enum ReturnStatus
    {
        REQUESTED,
        APPROVED,
        REJECTED,
        PICKED_UP,
        COMPLETED
    }
}
