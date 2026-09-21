package com.nology.employeemanager.common.dtos;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageResponse<R> {
    private int currentPage;
    private int totalPages;
    private long totalResults;
    private int resultsPerPage;
    private Integer nextPage;
    private Integer previousPage;
    private List<R> data;

    public static <T, R> PageResponse<R> assemble(Page<T> data, Function<T, R> mapper) {
        int currentPage = data.getNumber() + 1;
        int totalPages = data.getTotalPages();
        Integer nextPage = currentPage < data.getTotalPages() ? currentPage + 1 : null;
        Integer previousPage = currentPage > 1 ? currentPage - 1 : null;

        return new PageResponse<>(
                currentPage,
                totalPages,
                data.getTotalElements(),
                data.getSize(),
                nextPage,
                previousPage,
                data.map(mapper).getContent());
    }

    public PageResponse(
            int currentPage,
            int totalPages,
            long totalResults,
            int resultsPerPage,
            Integer nextPage,
            Integer previousPage,
            List<R> data) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
        this.data = data;
    }
}
