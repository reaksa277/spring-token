package com.example.demo.dto.external;

import lombok.Data;

@Data
public class JsonPlaceholderCommentsDto {
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;
}
