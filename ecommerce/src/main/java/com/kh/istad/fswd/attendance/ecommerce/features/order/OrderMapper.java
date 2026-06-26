package com.kh.istad.fswd.attendance.ecommerce.features.order;

import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  uses = OrderLineMapper.class)
public interface OrderMapper
{

    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "payment", ignore = true)
    OrderResponse mapOrderToOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "paymentQr", ignore = true)
    @Mapping(target = "paymentMd5", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    Order mapOrderRequestToOrder(OrderRequest orderRequest);


    // OrderLineResponse mapOrderToOrderLineResponse(Order order);
    // Order mapOrderLineRequestToOrder(OrderLineRequest orderLineRequest);
}
