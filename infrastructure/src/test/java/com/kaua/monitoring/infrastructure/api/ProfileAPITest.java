package com.kaua.monitoring.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.UserIdAlreadyExistsException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileCommand;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.CreateProfileOutput;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileUseCase;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.ProfileID;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import com.kaua.monitoring.infrastructure.ControllerTest;
import com.kaua.monitoring.infrastructure.profile.inputs.CreateProfileBody;
import com.kaua.monitoring.infrastructure.services.ProfileService;
import com.kaua.monitoring.infrastructure.services.gateways.JwtGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = { ProfileService.class, ProfileAPI.class })
public class ProfileAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private JwtGateway jwtGateway;

    @MockBean
    private CreateProfileUseCase createProfileUseCase;

    @MockBean
    private GetProfileByUserIdUseCase getProfileByUserIdUseCase;

    @MockBean
    private UpdateProfileUseCase updateProfileUseCase;

    @MockBean
    private DeleteProfileUseCase deleteProfileUseCase;

    @Test
    public void givenAnValidValues_whenCallsCreateProfile_shouldReturnProfile() throws Exception {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedAvatarUrl = "url";
        final var expectedType = VersionAccountType.FREE.name();

        final var aProfile = Profile.newProfile(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        final var aBody = new CreateProfileBody(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        when(jwtGateway.extractTokenSubject(any()))
                .thenReturn(expectedUserId);

        when(createProfileUseCase.execute(any()))
                .thenReturn(Either.right(CreateProfileOutput.from(aProfile)));

        final var request = MockMvcRequestBuilders.post("/profile")
                .header("authorization", "any-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileId", equalTo(aProfile.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", equalTo(expectedUserId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo(expectedUsername)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avatarUrl", equalTo(expectedAvatarUrl)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", equalTo(expectedType)));
    }

    @Test
    public void givenAnValidValuesAndInvalidUserId_whenCallsCreateProfile_shouldReturnUserIdDoesNotMatchException() throws Exception {
        final var expectedUserId = "456";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedAvatarUrl = "url";

        final var expectedErrorMessage = "UserId does not match";

        final var aProfile = Profile.newProfile(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        final var aBody = new CreateProfileBody(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        when(jwtGateway.extractTokenSubject(any()))
                .thenReturn("123");

        when(createProfileUseCase.execute(any()))
                .thenReturn(Either.right(CreateProfileOutput.from(aProfile)));

        final var request = MockMvcRequestBuilders.post("/profile")
                .header("authorization", "any-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAnInvalidValues_whenCallsCreateProfile_shouldReturnDomainException() throws Exception {
        final var expectedUserId = "123";
        final var expectedUsername = " ";
        final String expectedEmail = null;
        final var expectedAvatarUrl = "url";

        final var expectedErrorsMessage = List.of(
                new Error("'username' should not be null or empty"),
                new Error("'email' should not be null or empty")
        );

        final var aBody = new CreateProfileBody(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        when(jwtGateway.extractTokenSubject(any()))
                .thenReturn(expectedUserId);

        when(createProfileUseCase.execute(any()))
                .thenReturn(Either.left(DomainException.with(expectedErrorsMessage)));

        final var request = MockMvcRequestBuilders.post("/profile")
                .header("authorization", "any-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorsMessage.get(0).message())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].message", equalTo(expectedErrorsMessage.get(1).message())));
    }

    @Test
    public void givenAnValidValuesAndUserIdExists_whenCallsCreateProfile_shouldReturnUserIdAlreadyExistsException() throws Exception {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final String expectedEmail = "kaua@teste.com";
        final var expectedAvatarUrl = "url";

        final var expectedErrorMessage = "UserId already exists";

        final var aBody = new CreateProfileBody(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        when(jwtGateway.extractTokenSubject(any()))
                .thenReturn(expectedUserId);

        when(createProfileUseCase.execute(any()))
                .thenReturn(Either.left(DomainException.with(UserIdAlreadyExistsException.with())));

        final var request = MockMvcRequestBuilders.post("/profile")
                .header("authorization", "any-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAnValidValuesAndInvalidAuthorizationHeader_whenCallsCreateProfile_shouldReturnInterlErrorException() throws Exception {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final String expectedEmail = "kaua@teste.com";
        final var expectedAvatarUrl = "url";

        final var expectedErrorMessage = "Erro inesperado";

        final var aBody = new CreateProfileBody(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        when(createProfileUseCase.execute(any()))
                .thenReturn(Either.left(DomainException.with(UserIdAlreadyExistsException.with())));

        final var request = MockMvcRequestBuilders.post("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAnValidUserId_whenCallsGetByUserId_shouldReturnProfile() throws Exception {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedAvatarUrl = "url";
        final var expectedType = VersionAccountType.FREE.name();

        final var aProfile = Profile.newProfile(
                expectedUserId,
                expectedUsername,
                expectedEmail,
                expectedAvatarUrl
        );

        final var expectedId = aProfile.getId().getValue();

        when(getProfileByUserIdUseCase.execute(any()))
                .thenReturn(ProfileOutput.from(aProfile));

        final var request = MockMvcRequestBuilders.get("/profile/{id}", expectedUserId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileId", equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", equalTo(expectedUserId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo(expectedUsername)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avatarUrl", equalTo(expectedAvatarUrl)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", equalTo(expectedType)));
    }

    @Test
    public void givenAnInvalidUserId_whenCallsGetByUserId_shouldReturnNotFoundException() throws Exception {
        final var expectedUserId = "123";
        final var expectedErrorMessage = "Profile with ID 123 was not found";

        when(getProfileByUserIdUseCase.execute(any()))
                .thenThrow(new NotFoundException(Profile.class, expectedUserId));

        final var request = MockMvcRequestBuilders.get("/profile/{id}", expectedUserId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAnValidValuesAndProfileId_whenCallsUpdateProfile_shouldReturnProfile() throws Exception {
        final var expectedUserId = "123";
        final var expectedUsername = "kaua";
        final var expectedEmail = "kaua@teste.com";
        final var expectedType = VersionAccountType.PREMIUM.name();
        final var expectedId = "123456";

        when(updateProfileUseCase.execute(any()))
                .thenReturn(Either.right(
                        ProfileOutput.from(
                                Profile.with(
                                        ProfileID.from(expectedId),
                                        expectedUserId,
                                        expectedUsername,
                                        expectedEmail,
                                        null,
                                        VersionAccountType.valueOf(expectedType)
                                ))));

        final var request = MockMvcRequestBuilders.put("/profile/{id}", expectedId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("username", expectedUsername)
                .param("type", expectedType);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileId", equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", equalTo(expectedUserId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo(expectedUsername)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(expectedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", equalTo(expectedType)));
    }

    @Test
    public void givenAnInvalidValuesAndProfileId_whenCallsUpdateProfile_shouldReturnDomainException() throws Exception {
        final var expectedProfileId = "123";
        final String expectedUsername = null;
        final var expectedType = VersionAccountType.FREE.name();

        final var expectedErrorMessage = new Error("'username' should not be null or empty");

        when(updateProfileUseCase.execute(any()))
                .thenThrow(DomainException.with(expectedErrorMessage));

        final var request = MockMvcRequestBuilders.put("/profile/{id}", expectedProfileId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("username", expectedUsername)
                .param("type", expectedType);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage.message())));
    }

    @Test
    public void givenAnValidProfileId_whenCallsDeleteByProfileId_shouldReturnOk() throws Exception {
        final var expectedId = "123";

        final var aCommand = new DeleteProfileCommand(expectedId);

        doNothing()
                .when(deleteProfileUseCase).execute(aCommand);

        final var request = MockMvcRequestBuilders.delete("/profile/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
