package com.kh.istad.fswd.attendance.ecommerce.features.order;

import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.CreateOrderRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.UpdatePaymentStatusRequest;
import com.kh.istad.ite.payment.paymentservice.dto.BakongResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
       return  orderService.findAll(page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public OrderResponse findById(
            @PathVariable UUID id )
    {
        return orderService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/place-order")
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderService.createOrder(request);
    }

    @PostMapping("/{id}/check-payment")
    public BakongResponse checkPayment(
            @PathVariable UUID id
    ) {
        return orderService.checkPayment(id);
    }

    @PatchMapping("/{id}/cancel")
    public OrderResponse cancel(
            @PathVariable UUID id
    ) {
        return orderService.cancel(id);
    }

    @DeleteMapping("/{id}/soft-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDelete(
            @PathVariable UUID id
    ) {
        orderService.softDelete(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hardDelete(
            @PathVariable UUID id
    ) {
        orderService.hardDelete(id);
    }

    @PatchMapping("/{id}/payment-status")
    public OrderResponse updatePaymentStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        return orderService.updatePaymentStatus(id, request.paymentStatus());
    }
}
