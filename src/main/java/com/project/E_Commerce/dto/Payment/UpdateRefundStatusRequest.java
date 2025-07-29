package com.project.E_Commerce.dto.Payment;

import com.project.E_Commerce.Entity.Payment.Refund;
import lombok.Data;

@Data
public  class UpdateRefundStatusRequest {
    private Refund.RefundStatus status;
}

