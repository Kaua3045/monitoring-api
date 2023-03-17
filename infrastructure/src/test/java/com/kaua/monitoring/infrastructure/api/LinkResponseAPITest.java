package com.kaua.monitoring.infrastructure.api;

import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.usecases.checking.outputs.LinkResponseOutput;
import com.kaua.monitoring.application.usecases.checking.retrieve.ListLinkResponseByUrlIdUseCase;
import com.kaua.monitoring.domain.checking.LinkResponse;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.ControllerTest;
import com.kaua.monitoring.infrastructure.services.LinkResponseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = {LinkResponseService.class, LinkResponseAPI.class})
public class LinkResponseAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ListLinkResponseByUrlIdUseCase listLinkResponseByUrlIdUseCase;

    @Test
    public void givenAnValidParams_whenCallsListLInkResponseByUrlId_shouldReturnLinks() throws Exception {
        final var aLink = Link.newLink(
                "teste",
                "https://teste.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.EVERY_DAYS,
                Profile.newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                )
        );

        final var aLinkResponse = LinkResponse.newLinkResponse(
                "OK",
                200,
                Instant.now(),
                aLink
        );

        final var expectedUrlId = aLink.getId().getValue();
        final var expectedLinksResponses = List.of(
                LinkResponseOutput.from(aLinkResponse)
        );
        final var startTimestamp = LocalDateTime.now().minus(10, ChronoUnit.DAYS);
        final var endTimestamp = LocalDateTime.now().plus(10, ChronoUnit.DAYS);

        final var convertToLocalDateTime = LocalDateTime
                .ofInstant(aLinkResponse.getVerifiedDate(), ZoneId.of("America/Sao_Paulo"));
        final var expectedVerifiedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .format(convertToLocalDateTime);

        when(listLinkResponseByUrlIdUseCase.execute(any()))
                .thenReturn(expectedLinksResponses);

        final var request = MockMvcRequestBuilders.get("/links-responses/{id}", expectedUrlId)
                .queryParam("start", String.valueOf(startTimestamp))
                .queryParam("end", String.valueOf(endTimestamp))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", equalTo(aLinkResponse.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].responseMessage", equalTo(aLinkResponse.getResponseMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].statusCode", equalTo(aLinkResponse.getStatusCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].verifiedDate", equalTo(expectedVerifiedDate)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].urlId", equalTo(expectedUrlId)));

        verify(listLinkResponseByUrlIdUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedUrlId, cmd.urlId())
        ));
    }

    @Test
    public void givenAnValidParamsAndInvalidId_whenCallsListLInkResponseByUrlId_shouldReturnNotFoundException() throws Exception {
        final var expectedUrlId = "123";
        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(listLinkResponseByUrlIdUseCase.execute(any()))
                .thenThrow(new NotFoundException(Link.class, expectedUrlId));

        final var request = MockMvcRequestBuilders.get("/links-responses/{id}", expectedUrlId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(listLinkResponseByUrlIdUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedUrlId, cmd.urlId())
        ));
    }
}
