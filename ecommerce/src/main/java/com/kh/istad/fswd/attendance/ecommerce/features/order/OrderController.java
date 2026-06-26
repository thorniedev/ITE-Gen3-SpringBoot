package com.kh.istad.fswd.attendance.ecommerce.features.order;

import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderResponse;
import com.kh.istad.ite.payment.paymentservice.dto.BakongResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place-order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id) {
        return orderService.findById(id);
    }

    @PostMapping("/{id}/check-payment")
    public BakongResponse checkPayment(@PathVariable UUID id) {
        return orderService.checkPayment(id);
    }
}
