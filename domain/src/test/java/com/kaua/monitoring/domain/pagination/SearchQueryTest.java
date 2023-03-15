package com.kaua.monitoring.domain.pagination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchQueryTest {

    @Test
    public void givenAnValidValues_whenCallsNewSearchQuery_shouldReturnSearchQuery() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aSearchQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Assertions.assertNotNull(aSearchQuery);
        Assertions.assertEquals(expectedPage, aSearchQuery.page());
        Assertions.assertEquals(expectedPerPage, aSearchQuery.perPage());
        Assertions.assertEquals(expectedTerms, aSearchQuery.terms());
        Assertions.assertEquals(expectedSort, aSearchQuery.sort());
        Assertions.assertEquals(expectedDirection, aSearchQuery.direction());
    }
}
