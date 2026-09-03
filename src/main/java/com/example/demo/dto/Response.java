package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonPropertyOrder({"code", "message", "description", "timestamp", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Response<T> {

    private String code;
    private String message;
    private String description;
    private T data;
    private Long timestamp;

    private Response(String code, String message, String description, T data) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    private Response(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    public static Response<Void> success(String message, String description) {
        return new Response<>("200", message, description);
    }

    public static Response<Void> success(String code, String message, String description) {
        return new Response<>(code, message, description);
    }

    public static <T> Response<T> success(
            String code,
            String message,
            String description,
            T data) {

        return new Response<>(code, message, description, data);
    }

    public static Response<Void> error(
            String code,
            String message,
            String description) {

        return new Response<>(code, message, description);
    }

    public static <T> Response<T> error(
            String code,
            String message,
            String description,
            T data) {

        return new Response<>(code, message, description, data);
    }

    public static Response<Void> badRequest(
            String message,
            String description) {

        return new Response<>("400", message, description);
    }
}