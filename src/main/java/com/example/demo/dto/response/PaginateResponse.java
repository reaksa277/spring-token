package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PaginateResponse<T> {

    private List<T> content;
    private int page;
    private int totalPages;
    private long totalItems;

    public PaginateResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
    }
}