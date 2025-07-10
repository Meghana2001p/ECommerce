package com.project.E_Commerce.dto;

import com.project.E_Commerce.Entity.Refund;
import lombok.Data;

@Data
public  class UpdateRefundStatusRequest {
    private Refund.RefundStatus status;
}

