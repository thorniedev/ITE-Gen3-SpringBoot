package com.kh.istad.fswd.attendance.ecommerce.mapper;

import com.kh.istad.fswd.attendance.ecommerce.features.order.OrderLine;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderLineRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderLineMapper
{

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(
            target = "lineTotal",
            expression = "java(orderLine.getUnitPrice().multiply(java.math.BigDecimal.valueOf(orderLine.getQty())))"
    )
    OrderLineResponse mapOrderLineToOrderLineResponse(OrderLine orderLine);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderLine mapOrderLineRequestToOrderLine(OrderLineRequest orderLineRequest);

//    OrderLine mapOrderLineRequestToOrderLine (OrderLineRequest orderLineRequest);
//
//    OrderLineResponse mapOrderLineRequestToOrderLineResponse(OrderLine orderLine);
}
