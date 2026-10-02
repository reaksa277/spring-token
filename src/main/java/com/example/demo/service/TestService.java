package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
                .block();

        return response;
    }
}
