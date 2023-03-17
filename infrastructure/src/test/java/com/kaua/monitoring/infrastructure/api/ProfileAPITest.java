package com.kaua.monitoring.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.monitoring.application.exceptions.DomainException;
import com.kaua.monitoring.application.exceptions.NotFoundException;
import com.kaua.monitoring.application.exceptions.either.Either;
import com.kaua.monitoring.application.usecases.profile.create.CreateProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileCommand;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.application.usecases.profile.outputs.ProfileOutput;
import com.kaua.monitoring.application.usecases.profile.retrieve.get.GetProfileByUserIdUseCase;
import com.kaua.monitoring.application.usecases.profile.update.UpdateProfileUseCase;
import com.kaua.monitoring.domain.exceptions.Error;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.ProfileID;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import com.kaua.monitoring.infrastructure.ControllerTest;
import com.kaua.monitoring.infrastructure.profile.inputs.UpdateProfileBody;
import com.kaua.monitoring.infrastructure.services.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = {ProfileService.class, ProfileAPI.class })
public class ProfileAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateProfileUseCase createProfileUseCase;

    @MockBean
    private GetProfileByUserIdUseCase getProfileByUserIdUseCase;

    @MockBean
    private UpdateProfileUseCase updateProfileUseCase;

    @MockBean
    private DeleteProfileUseCase deleteProfileUseCase;

    // TODO: Make test with create profile

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
        final var expectedAvatarUrl = "url";
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
                                        expectedAvatarUrl,
                                        VersionAccountType.valueOf(expectedType)
                                ))));

        final var aBody = new UpdateProfileBody(
                expectedUsername,
                expectedAvatarUrl,
                expectedType
        );

        final var request = MockMvcRequestBuilders.put("/profile/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

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
    public void givenAnInvalidValuesAndProfileId_whenCallsUpdateProfile_shouldReturnDomainException() throws Exception {
        final var expectedProfileId = "123";
        final String expectedUsername = null;
        final var expectedAvatarUrl = "url";
        final var expectedType = VersionAccountType.FREE.name();

        final var expectedErrorMessage = new Error("'username' should not be null or empty");

        when(updateProfileUseCase.execute(any()))
                .thenThrow(DomainException.with(expectedErrorMessage));

        final var aBody = new UpdateProfileBody(
                expectedUsername,
                expectedAvatarUrl,
                expectedType
        );

        final var request = MockMvcRequestBuilders.put("/profile/{id}", expectedProfileId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aBody));

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
