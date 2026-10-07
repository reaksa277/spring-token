package com.example.demo.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendProductConfirmNotification(int productId, String text) {
        String threadName = Thread.currentThread().getName();

        log.info("[ASYNC-NOTIFICATION] Start sending notification to Telegram for Product: {} | Thread: {}",
                productId, threadName);

        try {
            Thread.sleep(10000);

            log.info("[ASYNC-NOTIFICATION] Send notification to Telegram Successfully for Product: {} | Thread: {}",
                    productId, threadName);
        } catch (InterruptedException e) {
            log.error("[ASYNC-NOTIFICATION] Failed to send notification for Product: {} | Error: {}",
                    productId, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
