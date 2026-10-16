package com.example.demo.controller;

import com.example.demo.dto.Response;
import com.example.demo.service.TestService;
import com.example.demo.service.kafka.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ProducerService kafkaProducer;

//    @PostMapping("/kafka-send-message")
//    public String sendEvent(@RequestBody String message) {
//        kafkaProducer.sendMessage(message);
//
//        return "success";
//    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot 123";
    }

    @GetMapping("/webclient-SynApi")
    public ResponseEntity<Response<Object>> testSynApi() {
        Object res = testService.testSynApi();

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200", "success", "success", res));
    }

    @GetMapping("/webclient-AsyncApi")
    public ResponseEntity<Response<Object>> testAsyncApi() {
        Object res = testService.testAsyncApi();

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200", "success", "success", res));
    }
}
