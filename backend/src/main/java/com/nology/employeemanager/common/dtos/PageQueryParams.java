package com.nology.employeemanager.common.dtos;

import org.springframework.data.domain.Page;

import com.nology.employeemanager.common.exceptions.UnprocessableContentException;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PageQueryParams {
    @Min(1)
    private Integer page = 1;

    @Min(1)
    @Max(20)
    private Integer size = 10;


    public <T> void validatePageNumber(Page<T> data) {
        if (getPage() > 1 && data.getTotalPages() < getPage()) {
            throw new UnprocessableContentException(String.format("Page %n is too high. Total pages is %n", getPage(), data.getTotalPages()));
        }
    }
}
