package com.example.demo.mapper;

import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.dto.request.OrderUpdateRequestDto;
import com.example.demo.dto.response.OrderItemResponseDto;
import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.model.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderEntity toEntity(OrderRequestDto dto) {
        OrderEntity entity = new OrderEntity();

        // order item entities
        List<OrderItemEntity> orderItemEntities = dto.getOrderItems()
                .stream()
                .map(orderItemDto -> {
                    OrderItemEntity orderItem = orderItemMapper.toEntity(orderItemDto);
                    orderItem.setOrder(entity);

                    return orderItem;
                })
                .toList();

        entity.setItems(orderItemEntities);

        return entity;
    }

    public OrderResponseDto toResponseDto(OrderEntity entity) {
        if(entity == null) {
            return null;
        }

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(entity.getOrderId());
        dto.setCreatedAt(entity.getCreatedOn());
        dto.setUpdatedAt(entity.getModifiedOn());
        dto.setStatus(entity.getStatus());

        if(entity.getItems() != null && !entity.getItems().isEmpty()) {
            List<OrderItemResponseDto> orderItemDtos = orderItemMapper.toResponseDtoList(entity.getItems());

            dto.setItems(orderItemDtos);

            // map for total price
            Double total = this.calculateTotalPrice(orderItemDtos);

            dto.setTotal(total);
        }

        return dto;
    }

    public List<OrderResponseDto> toResponseDtoList(List<OrderEntity> entities) {
        return entities.stream()
                .map(order -> this.toResponseDto(order))
                .toList();
    }

    public void updateEntityFromDto(OrderEntity entity, OrderUpdateRequestDto dto) {
        if(entity == null || dto == null) return;

        entity.setStatus(dto.getStatus());
    }

    private Double calculateTotalPrice(List<OrderItemResponseDto> items) {
        return items.stream()
                .mapToDouble(orderItemResponseDto -> {
                    return orderItemResponseDto.getPurchaseAmount() * orderItemResponseDto.getUnitPrice();
                })
                .sum();
    }
}
