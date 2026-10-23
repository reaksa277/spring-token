package com.example.demo.event;

import com.example.demo.common.enums.OrderStatus;
import com.example.demo.event.model.PaymentEvent;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.respository.OrderRepository;
import com.example.demo.service.kafka.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PaymentConsumer {
    @Autowired
    private ProducerService<PaymentEvent> producerService;

    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(
            topics = "payment.event",
            groupId = "payment-group",
            containerFactory = "paymentContainerFactory"
    )
    public void consume(PaymentEvent event) {
        log.info("[PAYMENT_EVENT] Processing order event with payment: {}", event);

        if (event.getStatus()) {
            Optional<OrderEntity> existingOrder = orderRepository.findById(event.getOrderId());

            if (existingOrder.isEmpty()) {
                log.error("Order is not found: {}", event.getOrderId());
            }

            OrderEntity order = existingOrder.get();

            order.setStatus(OrderStatus.SUCCESS.name());

            orderRepository.save(order);
        }
    }

}
