package com.project.E_Commerce.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "return_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Order item must not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @NotBlank(message = "Return reason must not be blank")
    @Size(max = 500, message = "Reason can't exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String reason;

    @NotNull(message = "Return status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnStatus status;

    @PastOrPresent(message = "Requested date can't be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    public enum ReturnStatus {
        REQUESTED,
        APPROVED,
        REJECTED,
        PICKED_UP,
        COMPLETED
    }
}
