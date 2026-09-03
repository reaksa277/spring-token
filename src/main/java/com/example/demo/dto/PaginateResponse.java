package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonPropertyOrder({"code", "message", "description", "timestamp", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PaginateResponse<T> {

    private String code;
    private String message;
    private String description;
    private T data;
    private Long timestamp;

    private PaginateResponse(String code, String message, String description, T data) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    private PaginateResponse(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    public static PaginateResponse<Void> success(String message, String description) {
        return new PaginateResponse<>("200", message, description);
    }

    public static PaginateResponse<Void> success(String code, String message, String description) {
        return new PaginateResponse<>(code, message, description);
    }

    public static <T> PaginateResponse<T> success(
            String code,
            String message,
            String description,
            T data) {

        return new PaginateResponse<>(code, message, description, data);
    }

    public static PaginateResponse<Void> error(
            String code,
            String message,
            String description) {

        return new PaginateResponse<>(code, message, description);
    }

    public static <T> PaginateResponse<T> error(
            String code,
            String message,
            String description,
            T data) {

        return new PaginateResponse<>(code, message, description, data);
    }

    public static PaginateResponse<Void> badRequest(
            String message,
            String description) {

        return new PaginateResponse<>("400", message, description);
    }
}