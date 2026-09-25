package com.example.demo.service.schedule;

import com.example.demo.service.security.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshTokenCleanupSchedule {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Scheduled(cron = "0 0 0/1 * * ?") // every 1 hour from hour 00:00
    public void cleanupExpiredAndRevokedToken() {
        log.info("Running cleanup job...");
        // execute clean up token
        refreshTokenService.deleteExpiredAndRevokedToken();
    }
}
