package com.kaua.monitoring.domain.pagination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PaginationTest {

    @Test
    public void givenAnValidValues_whenCallsNewPagination_shouldReturnPagination() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;
        final var expectedTotalPage = 0;
        final var expectedItems = List.of();

        final var aPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedTotalPage,
                expectedItems
        );

        Assertions.assertNotNull(aPagination);
        Assertions.assertEquals(expectedPage, aPagination.currentPage());
        Assertions.assertEquals(expectedPerPage, aPagination.perPage());
        Assertions.assertEquals(expectedTotal, aPagination.total());
        Assertions.assertEquals(expectedItems, aPagination.items());
    }

    @Test
    public void givenAnValidValues_whenCallsNewPaginationAndMap_shouldReturnPaginationMapped() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;
        final var expectedTotalPage = 0;
        final var expectedItems = List.of("a", "b", "c");

        final var aPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedTotalPage,
                expectedItems
        );

        final var actualPagination = aPagination.map(String::toUpperCase);

        Assertions.assertNotNull(actualPagination);
        Assertions.assertEquals(expectedPage, actualPagination.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPagination.perPage());
        Assertions.assertEquals(expectedTotal, actualPagination.total());
        Assertions.assertEquals(expectedItems.size(), actualPagination.items().size());
    }
}
