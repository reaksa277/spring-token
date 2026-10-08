package com.example.demo.service.mail;

import com.example.demo.common.config.ApplicationConfiguration;
import com.example.demo.common.wrapper.WebClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private ApplicationConfiguration appcofig;

    @Autowired
    WebClientWrapper webClientWrapper;

    @Async
    public void sendProductConfirmNotification(int productId, String text) {
        String threadName = Thread.currentThread().getName();

        log.info("[ASYNC-NOTIFICATION] Start sending notification to Telegram for Product: {} | Thread: {}",
                productId, threadName);

        try {
            Thread.sleep(10000);
            this.sendNotification("Hello from Spring Boot");

            log.info("[ASYNC-NOTIFICATION] Send notification to Telegram Successfully for Product: {} | Thread: {}",
                    productId, threadName);
        } catch (InterruptedException e) {
            log.error("[ASYNC-NOTIFICATION] Failed to send notification for Product: {} | Error: {}",
                    productId, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void sendNotification(String content) {
        String url = appcofig.getTelegram().getBaseUrl();
        String chatId = appcofig.getTelegram().getChatId();

        Map<String, String> payload = new HashMap<>();
        payload.put("chat_id", chatId);
        payload.put("text", content);
        payload.put("parse_mode", "HTML");

        webClientWrapper.postSync(url, payload, Object.class);
    }
}
