package com.example.demo.service;

import com.example.demo.common.config.ApplicationConfiguration;
import com.example.demo.common.wrapper.WebClientWrapper;
import com.example.demo.dto.external.JsonPlaceholderCommentsDto;
import com.example.demo.dto.external.JsonPlaceholderPostsDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonPlaceholderService {

    @Autowired
    private WebClientWrapper webClientWrapper;

    @Autowired
    private ApplicationConfiguration appConfig;

    private String BASE_URL;
    private String POSTS_URI;
    private String COMMENTS_URI;

    @PostConstruct
    private void init() {
        this.BASE_URL = appConfig.getJsonPlaceholder().getBaseUrl();
        this.POSTS_URI = appConfig.getJsonPlaceholder().getPostsUri();
        this.COMMENTS_URI = appConfig.getJsonPlaceholder().getCommentsUri();
    }

    public List<JsonPlaceholderPostsDto> getPosts() {
        String url = BASE_URL.concat(POSTS_URI);

        List<JsonPlaceholderPostsDto> response = (List<JsonPlaceholderPostsDto>) webClientWrapper.getSync(url, List.class);

        return response;
    }

    public List<JsonPlaceholderCommentsDto> getComments() {
        String url = BASE_URL.concat(COMMENTS_URI);

        List<JsonPlaceholderCommentsDto> response = (List<JsonPlaceholderCommentsDto>) webClientWrapper.getSync(url, List.class);

        return response;
    }

    public JsonPlaceholderPostsDto createPost(JsonPlaceholderPostsDto payload) {
        String url = BASE_URL.concat(POSTS_URI);

        JsonPlaceholderPostsDto response = webClientWrapper.postSync(url, payload, JsonPlaceholderPostsDto.class);

        return response;
    }
}
