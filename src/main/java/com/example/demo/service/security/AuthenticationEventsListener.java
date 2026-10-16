package com.example.demo.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEventsListener {

    @Autowired
    private LoginAttemptsService loginAttemptsService;

    @EventListener
    public void onBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        loginAttemptsService.recordFailure(event.getAuthentication().getName());
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        loginAttemptsService.reset(event.getAuthentication().getName());
    }
}
