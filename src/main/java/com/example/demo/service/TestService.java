package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class TestService {

    private WebClient webClient = WebClient
            .builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build();

    public Object testSynApi() {
        String uri = "/albums";

        log.info("TEST: Before API request");

        var response = webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofMillis(2000))
                .block();

        log.info("TEST: Getting Response");

        log.info("TEST: After API request");

        return response;
    }

    public Object testAsyncApi() {
        String uri = "/users";

        log.info("TEST: Before API request");

        Mono<Object> response = webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class);

        response.subscribe(res -> {
            log.info("TEST: Getting Response");
            log.info("TEST: API response: {}", res);
        });

        log.info("TEST: After API request");

        return response;
    }
}
