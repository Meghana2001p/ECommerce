package com.project.E_Commerce.dto.Return;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequestDTO {

    private Integer orderId;
    private String reason;
}
