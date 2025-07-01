package com.project.E_Commerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "gift_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Order must not be null")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Size(max = 500, message = "Gift message must not exceed 500 characters")
    @Column(name = "gift_message", length = 500)
    private String giftMessage;

    @Column(name = "hide_price", nullable = false)
    private Boolean hidePrice = false;

    @Column(name = "gift_wrapping", nullable = false)
    private Boolean giftWrapping = false;
}
