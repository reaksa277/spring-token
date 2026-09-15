package com.example.demo.common.util;

import com.example.demo.dto.request.RequestPageDto;
import com.example.demo.exception.BusinessException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PageRequestValidationUtils {

    public void validatePaginationRequest(RequestPageDto requestDto) throws BusinessException {
        if (requestDto.getPage() == null || requestDto.getPage() < 1) {
            throw new BusinessException("400", "page must be greater than or equal to 1");
        }
        if (requestDto.getSize() == null || requestDto.getSize() < 1) {
            throw new BusinessException("400", "size must be greater than or equal to 1");
        }
    }
}
