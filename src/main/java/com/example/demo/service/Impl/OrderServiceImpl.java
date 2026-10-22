package com.example.demo.service.Impl;

import com.example.demo.dto.request.OrderItemRequestDto;
import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.exception.model.product.ProductNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.respository.OrderRepository;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    @Override
    @Transactional
    public void placeOrder(OrderRequestDto payload) {
        String threadName = Thread.currentThread().getName();;

        validateProductCodes(payload);

        log.info("[SYNC-ORDER] Creating new order | Thread: {}", threadName);

        OrderEntity order = mapper.toEntity(payload);

        orderRepository.save(order);
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
}
