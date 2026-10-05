package com.example.demo.controller;

import com.example.demo.dto.Response;
import com.example.demo.dto.external.JsonPlaceholderCommentsDto;
import com.example.demo.dto.external.JsonPlaceholderPostsDto;
import com.example.demo.service.JsonPlaceholderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json-placeholder")
public class JsonPlaceholder {

    @Autowired
    private JsonPlaceholderService jsonPlaceholderService;

    @PostMapping("/posts")
    public ResponseEntity<Response<JsonPlaceholderPostsDto>> createPost(@RequestBody JsonPlaceholderPostsDto payload) {
        JsonPlaceholderPostsDto post = jsonPlaceholderService.createPost(payload);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success("200", "success", "success", post));
    }

    @GetMapping("/posts")
    public ResponseEntity<Response<?>> getPosts() {
        List<JsonPlaceholderPostsDto> posts = jsonPlaceholderService.getPosts();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success("200", "success", "success", posts));
    }

    @GetMapping("/comments")
    public ResponseEntity<Response<?>> getComments() {
        List<JsonPlaceholderCommentsDto> comments = jsonPlaceholderService.getComments();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success("200", "success", "success", comments));
    }
}
