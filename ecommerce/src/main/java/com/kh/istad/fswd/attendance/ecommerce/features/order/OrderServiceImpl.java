package com.kh.istad.fswd.attendance.ecommerce.features.order;

import com.kh.istad.fswd.attendance.common.exception.BadRequestException;
import com.kh.istad.fswd.attendance.common.exception.ResourceNotFoundException;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.*;
import com.kh.istad.fswd.attendance.ecommerce.features.product.Product;
import com.kh.istad.fswd.attendance.ecommerce.features.product.ProductRepository;
import com.kh.istad.ite.payment.paymentservice.dto.BakongRequest;
import com.kh.istad.ite.payment.paymentservice.dto.BakongResponse;
import com.kh.istad.ite.payment.paymentservice.dto.CheckTransactionRequest;
import com.kh.istad.ite.payment.paymentservice.service.BakongService;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    // ** One save all Tables Hibernate behavior.
    // Transaction strong with Hibernate
    // CascadeType:
    // - ALL:
    // - ...

    private static final String PAYMENT_PENDING = "PENDING";
    private static final String PAYMENT_PAID = "PAID";
    private static final String PAYMENT_CANCELLED = "CANCELLED";
    private static final String PAYMENT_FAILED = "FAILED";
    private static final String PAYMENT_EXPIRED = "EXPIRED";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BakongService bakongService;

    @Value("${ecommerce.payment.currency:USD}") // Reads default currency from config, If missing, use USD
    private KHQRCurrency defaultPaymentCurrency;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, String customerId) {

        Order order = new Order();

        // Copies customer info from request into order
        order.setCustomerId(customerId);
        order.setAddress(request.address());
        order.setDiscount(request.discount() == null ? 0F : request.discount()); // If discount is null, use dis = 0
        order.setStatus(false); // default status of payment (Order is not paid yet)
        order.setPhone(request.phone());
        order.setEmail(request.email());
        order.setRemark(request.remark());

        // Set default order data
        order.setCreatedDate(LocalDate.now());
        order.setIsDeleted(false);
        order.setPaymentStatus(PAYMENT_PENDING);

        // Prepare list for products in the order and subtotal calculate
        List<OrderLine> orderLines = new ArrayList<>();
        BigDecimal subTotal = BigDecimal.ZERO;

        for (OrderLineRequest lineRequest : request.orderLines()) {
            Product product = productRepository.findById(lineRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", lineRequest.productId()));

            validateProduct(product, lineRequest.qty());

            OrderLine orderLine = new OrderLine();

            // Connect order line to order and product.
            // Save current product price into order line
            orderLine.setOrder(order);
            orderLine.setProduct(product);
            orderLine.setQty(lineRequest.qty());
            orderLine.setUnitPrice(product.getUnitPrice());

            // Add line to order
            orderLines.add(orderLine);

            // Reduce stock
            product.setQty(product.getQty() - lineRequest.qty());
            productRepository.save(product);

            subTotal = subTotal.add(product.getUnitPrice().multiply(BigDecimal.valueOf(lineRequest.qty())));
        }

        // Add (unitPrice * qty) to subtotal
        BigDecimal discount = BigDecimal.valueOf(order.getDiscount());

        // If discount bigger than subtotal => reject
        if (discount.compareTo(subTotal) > 0) {
            throw new BadRequestException("Discount cannot be greater than order subtotal");
        }

        // Calculate final total
        BigDecimal total = subTotal.subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Order total must be greater than zero");
        }

        // Attach all order lines to order
        order.setOrderLines(orderLines);

        // Generate Bakong-QR and save payment[ Md5/paymentQr] into order
        addPaymentQr(order, request, total);

        // Save order and order lines
        Order savedOrder = orderRepository.save(order);

        // Return DTO response to client.
        return toResponse(savedOrder, subTotal, total);
    }

    @Override
    @Transactional(readOnly = true) // no update
    public OrderResponse findById(UUID id) {
        Order order = findOrder(id);
        return toResponse(order, calculateSubTotal(order), calculateTotal(order));
    }

    @Override
    public Page<OrderResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<Order> orders = orderRepository.findByIsDeletedFalse(pageRequest);

        return orders.map(order ->
                toResponse(order, calculateSubTotal(order), calculateTotal(order))
        );
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID id) {
        Order order = findOrder(id);
        cancelOrder(order);
        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder, calculateSubTotal(savedOrder), calculateTotal(savedOrder));
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        Order order = findOrder(id);
        if (PAYMENT_PAID.equals(order.getPaymentStatus()) || Boolean.TRUE.equals(order.getStatus())) {
            throw new BadRequestException("Paid order cannot be soft deleted");
        }

        if (PAYMENT_PENDING.equals(order.getPaymentStatus())) {
            cancelOrder(order);
        }

        order.setIsDeleted(true);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void hardDelete(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        if (PAYMENT_PAID.equals(order.getPaymentStatus()) || Boolean.TRUE.equals(order.getStatus())) {
            throw new BadRequestException("Paid order cannot be hard deleted");
        }

        if (PAYMENT_PENDING.equals(order.getPaymentStatus())) {
            throw new BadRequestException("Cancel order before hard delete");
        }

        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public OrderResponse updatePaymentStatus(UUID id, String paymentStatus) {
        Order order = findOrder(id);
        String normalizedStatus = normalizePaymentStatus(paymentStatus);

        if (PAYMENT_PAID.equals(order.getPaymentStatus()) && !PAYMENT_PAID.equals(normalizedStatus)) {
            throw new BadRequestException("Paid order payment status cannot be changed");
        }

        if (PAYMENT_PAID.equals(normalizedStatus)) {
            if (PAYMENT_CANCELLED.equals(order.getPaymentStatus())
                    || PAYMENT_FAILED.equals(order.getPaymentStatus())
                    || PAYMENT_EXPIRED.equals(order.getPaymentStatus())) {
                throw new BadRequestException("Cannot mark restored-stock order as paid");
            }
            order.setStatus(true);
        } else {
            if (PAYMENT_PENDING.equals(order.getPaymentStatus())
                    && (PAYMENT_CANCELLED.equals(normalizedStatus)
                    || PAYMENT_FAILED.equals(normalizedStatus)
                    || PAYMENT_EXPIRED.equals(normalizedStatus))) {
                restoreStock(order);
            }
            order.setStatus(false);
        }

        order.setPaymentStatus(normalizedStatus);
        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder, calculateSubTotal(savedOrder), calculateTotal(savedOrder));
    }

    // Checks if Bakong transaction is paid
    @Override
    @Transactional
    public BakongResponse checkPayment(UUID id) {
        Order order = findOrder(id);
        if (order.getPaymentMd5() == null || order.getPaymentMd5().isBlank()) {
            throw new BadRequestException("Order does not have a Bakong payment MD5");
        }

        BakongResponse response = bakongService.checkTransactionByMD5(
                new CheckTransactionRequest(order.getPaymentMd5())
        );

        // Bakong => payment success
        if (response.responseCode() == 0) {

            // Mark order as paid
            order.setStatus(true);
            order.setPaymentStatus(PAYMENT_PAID);
            orderRepository.save(order);
        }

        return response;
    }

    private void cancelOrder(Order order) {
        if (PAYMENT_PAID.equals(order.getPaymentStatus()) || Boolean.TRUE.equals(order.getStatus())) {
            throw new BadRequestException("Paid order cannot be cancelled");
        }

        if (PAYMENT_CANCELLED.equals(order.getPaymentStatus())) {
            return;
        }

        if (!PAYMENT_PENDING.equals(order.getPaymentStatus())) {
            throw new BadRequestException("Only pending order can be cancelled");
        }

        restoreStock(order);
        order.setStatus(false);
        order.setPaymentStatus(PAYMENT_CANCELLED);
    }

    private void restoreStock(Order order) {
        if (order.getOrderLines() == null) {
            return;
        }

        for (OrderLine orderLine : order.getOrderLines()) {
            Product product = orderLine.getProduct();
            if (product != null) {
                product.setQty(product.getQty() + orderLine.getQty());
                productRepository.save(product);
            }
        }
    }

    private String normalizePaymentStatus(String paymentStatus) {
        String normalizedStatus = paymentStatus == null ? "" : paymentStatus.trim().toUpperCase();
        if (!PAYMENT_PENDING.equals(normalizedStatus)
                && !PAYMENT_PAID.equals(normalizedStatus)
                && !PAYMENT_CANCELLED.equals(normalizedStatus)
                && !PAYMENT_FAILED.equals(normalizedStatus)
                && !PAYMENT_EXPIRED.equals(normalizedStatus)) {
            throw new BadRequestException("Invalid payment status");
        }
        return normalizedStatus;
    }

    private void validateProduct(Product product, Integer requestedQty) {
        if (Boolean.TRUE.equals(product.getIsDeleted())) {
            throw new BadRequestException("Product is deleted: " + product.getId());
        }
        if (Boolean.FALSE.equals(product.getIsAvailable())) {
            throw new BadRequestException("Product is not available: " + product.getId());
        }
        if (product.getQty() == null || product.getQty() < requestedQty) {
            throw new BadRequestException("Insufficient stock for product: " + product.getId());
        }
    }

    private void addPaymentQr(Order order, CreateOrderRequest request, BigDecimal total) {
        KHQRCurrency currency = request.paymentCurrency() == null
                ? defaultPaymentCurrency
                : request.paymentCurrency();

        BakongRequest bakongRequest = new BakongRequest(
                currency,
                total.doubleValue(),
                "ITE ECOMMERCE",
                "PHNOM PENH",
                order.getCustomerId(),
                "ECOMMERCE",
                null,
                15,
                generateBillNumber(),
                "ECOMMERCE",
                "ONLINE",
                order.getPhone(),
                "Order Payment",
                null,
                null,
                null
        );

        KHQRResponse<KHQRData> qrResponse = bakongService.generateQR(bakongRequest);
        if (qrResponse.getKHQRStatus() == null || qrResponse.getKHQRStatus().getCode() != 0) {
            String message = qrResponse.getKHQRStatus() == null
                    ? "Unable to generate Bakong QR"
                    : qrResponse.getKHQRStatus().getMessage();
            throw new BadRequestException(message == null ? "Unable to generate Bakong QR" : message);
        }

        order.setPaymentMd5(qrResponse.getData().getMd5());
        order.setPaymentQr(qrResponse.getData().getQr());
    }

    private String generateBillNumber() {
        return "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private Order findOrder(UUID id) {
        return orderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    private BigDecimal calculateSubTotal(Order order) {
        if (order.getOrderLines() == null) {
            return BigDecimal.ZERO;
        }

        return order.getOrderLines().stream()
                .map(line -> line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotal(Order order) {
        return calculateSubTotal(order).subtract(BigDecimal.valueOf(order.getDiscount() == null ? 0F : order.getDiscount()));
    }

    private OrderResponse toResponse(Order order, BigDecimal subTotal, BigDecimal total) {
        List<OrderLineResponse> lines = order.getOrderLines() == null
                ? List.of()
                : order.getOrderLines().stream()
                .map(this::toLineResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getAddress(),
                order.getDiscount(),
                order.getStatus(),
                order.getPhone(),
                order.getEmail(),
                order.getRemark(),
                order.getCreatedDate(),
                order.getIsDeleted(),
                new PaymentQrResponse(order.getPaymentStatus(), order.getPaymentMd5(), order.getPaymentQr()),
                lines,
                subTotal,
                total
        );
    }

    private OrderLineResponse toLineResponse(OrderLine orderLine) {
        BigDecimal lineTotal = orderLine.getUnitPrice().multiply(BigDecimal.valueOf(orderLine.getQty()));
        Product product = orderLine.getProduct();

        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQty(),
                orderLine.getUnitPrice(),
                product == null ? null : product.getId(),
                product == null ? null : product.getName(),
                lineTotal
        );
    }

}
