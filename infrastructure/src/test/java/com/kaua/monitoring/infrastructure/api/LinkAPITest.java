package com.kaua.monitoring.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.link.create.CreateLinkUseCase;
import com.kaua.monitoring.application.usecases.link.delete.DeleteLinkUseCase;
import com.kaua.monitoring.application.usecases.link.outputs.CreateLinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.LinkOutput;
import com.kaua.monitoring.application.usecases.link.outputs.UpdateLinkOutput;
import com.kaua.monitoring.application.usecases.link.retrieve.get.GetLinkByIdUseCase;
import com.kaua.monitoring.application.usecases.link.retrieve.list.profileId.ListLinkByProfileIdUseCase;
import com.kaua.monitoring.application.usecases.link.update.UpdateLinkUseCase;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.links.Link;
import com.kaua.monitoring.domain.links.LinkExecutions;
import com.kaua.monitoring.domain.pagination.Pagination;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.infrastructure.ControllerTest;
import com.kaua.monitoring.infrastructure.link.inputs.CreateLinkBody;
import com.kaua.monitoring.infrastructure.link.inputs.UpdateLinkBody;
import com.kaua.monitoring.infrastructure.services.LinkService;
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
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = {LinkService.class, LinkAPI.class})
public class LinkAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateLinkUseCase createLinkUseCase;

    @MockBean
    private GetLinkByIdUseCase getLinkByIdUseCase;

    @MockBean
    private ListLinkByProfileIdUseCase listLinkByProfileIdUseCase;

    @MockBean
    private UpdateLinkUseCase updateLinkUseCase;

    @MockBean
    private DeleteLinkUseCase deleteLinkUseCase;

    @Test
    public void givenAnValidValues_whenCallsCreate_shouldReturnLinkId() throws Exception {
        final var expectedTitle = "Teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = "2023-03-17T13:22:00.00";
        final var expectedLinkExecution = "NO_REPEAT";
        final var expectedProfileId = "123";

        final var aBody = new CreateLinkBody(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution,
                expectedProfileId
        );

        when(createLinkUseCase.execute(any()))
                .thenReturn(Either.right(new CreateLinkOutput("1234567")));

        final var request = MockMvcRequestBuilders.post("/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo("1234567")));

        verify(createLinkUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedTitle, cmd.title())
                        && Objects.equals(expectedUrl, cmd.url())
                        && Objects.nonNull(cmd.executeDate())
                        && Objects.equals(expectedLinkExecution, cmd.linkExecution())
                        && Objects.equals(expectedProfileId, cmd.profileId())));
    }

    @Test
    public void givenAnInvalidValues_whenCallsCreate_shouldReturnDomainException() throws Exception {
        final var expectedTitle = " ";
        final var expectedUrl = "teste";
        final var expectedExecuteDate = "2023-03-17T13:22:00.00";
        final var expectedLinkExecution = "NO_REPEAT";
        final String expectedProfileId = null;

        final var aBody = new CreateLinkBody(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution,
                expectedProfileId
        );

        final var expectedErrorsMessage = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url"),
                new Error("'profile' should not be null")
        );

        when(createLinkUseCase.execute(any()))
                .thenReturn(Either.left(DomainException.with(expectedErrorsMessage)));

        final var request = MockMvcRequestBuilders.post("/links")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorsMessage.get(0).message())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].message", equalTo(expectedErrorsMessage.get(1).message())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[2].message", equalTo(expectedErrorsMessage.get(2).message())));

        verify(createLinkUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedTitle, cmd.title())
                        && Objects.equals(expectedUrl, cmd.url())
                        && Objects.nonNull(cmd.executeDate())
                        && Objects.equals(expectedLinkExecution, cmd.linkExecution())
                        && Objects.equals(expectedProfileId, cmd.profileId())));
    }

    @Test
    public void givenAnValidId_whenCallsGetById_shouldReturnLink() throws Exception {
        final var expectedTitle = "Teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = Instant.now().plus(5, ChronoUnit.DAYS);
        final var expectedLinkExecution = LinkExecutions.EVERY_DAYS;
        final var expectedProfileId = Profile
                .newProfile(
                        "123",
                        "kaua",
                        "kaua@teste.com",
                        null
                );

        final var aLink = Link.newLink(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution,
                expectedProfileId
        );

        final var expectedId = aLink.getId().getValue();

        final var convertToLocalDateTime = LocalDateTime
                .ofInstant(expectedExecuteDate, ZoneId.of("America/Sao_Paulo"));
        final var expectedFormattedExecuteDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .format(convertToLocalDateTime);

        when(getLinkByIdUseCase.execute(any()))
                .thenReturn(LinkOutput.from(aLink));

        final var request = MockMvcRequestBuilders.get("/links/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", equalTo(expectedUrl)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.executeDateFormatted", equalTo(expectedFormattedExecuteDate)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.linkExecution", equalTo(expectedLinkExecution.name())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.profileId", equalTo(expectedProfileId.getId().getValue())));

        verify(getLinkByIdUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id()))
        );
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFoundException() throws Exception {
        final var expectedId = "123";
        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(getLinkByIdUseCase.execute(any()))
                .thenThrow(new NotFoundException(Link.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/links/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getLinkByIdUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id()))
        );
    }

    @Test
    public void givenAnValidParams_whenCallsListLinksByProfileId_shouldReturnLinks() throws Exception {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        final var aLink = Link.newLink(
                "Teste",
                "https://teste.com",
                Instant.now().plus(5, ChronoUnit.DAYS),
                LinkExecutions.EVERY_DAYS,
                aProfile
        );

        final var expectedProfileId = aProfile.getId().getValue();

        final var convertToLocalDateTime = LocalDateTime
                .ofInstant(aLink.getExecuteDate(), ZoneId.of("America/Sao_Paulo"));
        final var expectedFormattedExecuteDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .format(convertToLocalDateTime);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "teste";
        final var expectedSort = "url";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(LinkOutput.from(aLink));

        when(listLinkByProfileIdUseCase.execute(any()))
                .thenReturn(new Pagination<>(
                        expectedPage,
                        expectedPerPage,
                        expectedTotal,
                        expectedItems
                ));

        final var request = MockMvcRequestBuilders.get("/links/list/{id}", expectedProfileId)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", equalTo(aLink.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].title", equalTo(aLink.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].url", equalTo(aLink.getUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].executeDateFormatted", equalTo(expectedFormattedExecuteDate)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].linkExecution", equalTo(aLink.getLinkExecution().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].profileId", equalTo(expectedProfileId)));

        verify(listLinkByProfileIdUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.aQuery().page())
                        && Objects.equals(expectedPerPage, query.aQuery().perPage())
                        && Objects.equals(expectedDirection, query.aQuery().direction())
                        && Objects.equals(expectedSort, query.aQuery().sort())
                        && Objects.equals(expectedTerms, query.aQuery().terms())
                        && Objects.equals(expectedProfileId, query.profileId())
        ));
    }

    @Test
    public void givenAnValidValues_whenCallsUpdateLinkById_shouldReturnLinkId() throws Exception {
        final var expectedId = "123456";
        final var expectedTitle = "Teste";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = "2023-03-17T13:22:00.00";
        final var expectedLinkExecution = "NO_REPEAT";

        final var aBody = new UpdateLinkBody(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution
        );

        when(updateLinkUseCase.execute(any()))
                .thenReturn(Either.right(new UpdateLinkOutput(expectedId)));

        final var request = MockMvcRequestBuilders.put("/links/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(expectedId)));

        verify(updateLinkUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedTitle, cmd.title())
                        && Objects.equals(expectedUrl, cmd.url())
                        && Objects.nonNull(cmd.executeDate())
                        && Objects.equals(expectedLinkExecution, cmd.linkExecution())
        ));
    }

    @Test
    public void givenAnInvalidValues_whenCallsUpdateLinkById_shouldReturnDomainException() throws Exception {
        final var expectedId = "123";
        final var expectedTitle = " ";
        final var expectedUrl = "teste";
        final var expectedExecuteDate = "2023-03-17T13:22:00.00";
        final var expectedLinkExecution = "NO_REPEAT";

        final var aBody = new UpdateLinkBody(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution
        );

        final var expectedErrorsMessage = List.of(
                new Error("'title' should not be null or empty"),
                new Error("'url' you must provide a valid url")
        );

        when(updateLinkUseCase.execute(any()))
                .thenReturn(Either.left(DomainException.with(expectedErrorsMessage)));

        final var request = MockMvcRequestBuilders.put("/links/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorsMessage.get(0).message())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].message", equalTo(expectedErrorsMessage.get(1).message())));

        verify(updateLinkUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedTitle, cmd.title())
                        && Objects.equals(expectedUrl, cmd.url())
                        && Objects.nonNull(cmd.executeDate())
                        && Objects.equals(expectedLinkExecution, cmd.linkExecution())
        ));
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateLinkById_shouldReturnNotFoundException() throws Exception {
        final var expectedId = "123";
        final var expectedTitle = "aa";
        final var expectedUrl = "https://teste.com";
        final var expectedExecuteDate = "2023-03-17T13:22:00.00";
        final var expectedLinkExecution = "NO_REPEAT";

        final var aBody = new UpdateLinkBody(
                expectedTitle,
                expectedUrl,
                expectedExecuteDate,
                expectedLinkExecution
        );

        final var expectedErrorMessage = "Link with ID 123 was not found";

        when(updateLinkUseCase.execute(any()))
                .thenThrow(new NotFoundException(Link.class, expectedId));

        final var request = MockMvcRequestBuilders.put("/links/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateLinkUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedTitle, cmd.title())
                        && Objects.equals(expectedUrl, cmd.url())
                        && Objects.nonNull(cmd.executeDate())
                        && Objects.equals(expectedLinkExecution, cmd.linkExecution())
        ));
    }

    @Test
    public void givenAnValidId_whenCallsDeleteLinkById_shouldReturnNoContent() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteLinkUseCase).execute(any());

        final var request = MockMvcRequestBuilders.delete("/links/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteLinkUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
        ));
    }
}
