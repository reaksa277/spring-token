package com.example.demo.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerService<T> {

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;

//    public void sendMessage(String message) {
//        String topic = "testing-queue";
//        kafkaTemplate.send(topic, message);
//
//        log.info("message produced[{}]: {}", topic, message);
//    }

    public void sendMessage(String topic, T message) {
        kafkaTemplate.send(topic, message)
                .whenComplete((result, error) -> {
                    if(error == null) {
                        log.info("[SUCCESS] Sent message to topic [{}]: {}", topic, message);
                    } else {
                        log.error("[FAILED] Failed to send message to topic [{}]: {}", topic, message);
                    }
                });
    }
}
