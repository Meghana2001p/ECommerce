package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {

    private Integer id;
    @NotNull(message = "Order item ID must not be null")

    private Integer orderItemId;
    @NotBlank(message = "Return reason must not be blank")
    @Size(max = 500, message = "Reason can't exceed 500 characters")
    private String reason;
    @NotNull(message = "Return status must not be null")

    private ReturnStatus status;
    @PastOrPresent(message = "Requested date can't be in the future")
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
