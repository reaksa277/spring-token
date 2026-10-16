package com.example.demo.service.security;

import com.example.demo.common.config.ApplicationConfiguration;
import com.example.demo.respository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginAttemptsService {
    @Autowired
    private ApplicationConfiguration appConfig;

    @Autowired
    private UserRepository userRepository;

    private int maxAttempts;
    private Duration lockDuration;

    @PostConstruct
    private void init() {
        this.maxAttempts = appConfig.getLogin().getMaxAttempts();
        this.lockDuration = appConfig.getLogin().getLockDuration();
    }

    @Transactional
    public void recordFailure(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            if (user.getLockedUntil() != null) {
                if (!user.isAccountNonLocked()) return;   // still locked, don't extend
                // lock expired: start a fresh count
                user.setFailedAttempts(0);
                user.setLockedUntil(null);
            }

            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= this.maxAttempts) {
                user.setLockedUntil(Instant.now().plus(this.lockDuration));
                log.info("lock duration = {}", this.lockDuration);
            }
        });
    }

    @Transactional
    public void reset(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFailedAttempts(0);
            user.setLockedUntil(null);
        });

    }
}
