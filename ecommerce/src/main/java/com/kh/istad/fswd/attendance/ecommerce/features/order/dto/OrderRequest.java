package com.kh.istad.fswd.attendance.ecommerce.dto.order;

import com.kh.istad.fswd.attendance.ecommerce.dto.orderline.OrderLineRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequest(
        @NotBlank String customerId,
        @NotBlank String address,
        Float discount,
        String phone,
        @Email String email,
        String remark,
        @NotEmpty @Valid List<OrderLineRequest> orderLines
) {}