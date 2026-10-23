package com.example.demo.service.Impl;

import com.example.demo.dto.request.OrderItemRequestDto;
import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.dto.request.OrderUpdateRequestDto;
import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.event.model.OrderEvent;
import com.example.demo.event.model.ProductEvent;
import com.example.demo.exception.model.ResourceNotFoundException;
import com.example.demo.exception.model.product.ProductNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.respository.OrderRepository;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.kafka.ProducerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper mapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProducerService<OrderEvent> producerService;

    @Override
    @Transactional
    public void placeOrder(OrderRequestDto payload) {
        String threadName = Thread.currentThread().getName();;

        validateProductCodes(payload);

        log.info("[SYNC-ORDER] Creating new order | Thread: {}", threadName);

        OrderEntity order = mapper.toEntity(payload);

        orderRepository.save(order);

        producerService.sendMessage("order.event", new OrderEvent(order.getOrderId()));
    }

    private void validateProductCodes(OrderRequestDto payload) {
        Set<String> requestedCodes = payload.getOrderItems()
                .stream()
                .map(OrderItemRequestDto::getProductCode)
                .collect(Collectors.toSet());

        Set<String> existingCodes = productRepository.findExistingProductCodes(requestedCodes);

        Set<String> missingCodes = new HashSet<>(requestedCodes);
        missingCodes.removeAll(existingCodes);

        if (!missingCodes.isEmpty()) {
            throw new ProductNotFoundException(missingCodes);
        }
    }

    @Override
    @Transactional
    public List<OrderResponseDto> listOrders() {
        List<OrderEntity> orders = orderRepository.findAll();

        return mapper.toResponseDtoList(orders);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateRequestDto payload) {
        OrderEntity existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("order not found with id: " + orderId);
                });

        mapper.updateEntityFromDto(existingOrder, payload);
        orderRepository.save(existingOrder);

        return mapper.toResponseDto(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("order not found with id: " + orderId);
        }

        orderRepository.deleteById(orderId);
    }
}
