package com.example.demo.service.Impl;

import com.example.demo.dto.ProductResponseDto;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String MOCK_DATA_PATH = "mock-data/product-data.json";

    private final ObjectMapper mapper;

    @Override
    public List<ProductResponseDto> listProduct() {
        try (InputStream inputStream = new ClassPathResource(MOCK_DATA_PATH).getInputStream()) {
            return mapper.readValue(
                    inputStream,
                    mapper.getTypeFactory().constructCollectionType(List.class, ProductResponseDto.class)
            );
        } catch (IOException e) {
            // Fall back to an empty list rather than letting the endpoint 500 if the mock file is missing.
            System.err.println("Failed to load mock product data: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
