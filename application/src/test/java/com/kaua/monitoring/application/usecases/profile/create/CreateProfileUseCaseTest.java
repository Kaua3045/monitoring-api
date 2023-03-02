package com.kaua.monitoring.application.usecases.profile.create;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.profile.VersionAccountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProfileUseCaseTest {

    @InjectMocks
    private DefaultCreateProfileUseCase useCase;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidValues_whenCallsCreate_shouldReturnProfile() {
        final var expectedUserId = "123";
        final var expectedAvatarUrl = "url/imaginaria";
        final var expectedType = VersionAccountType.FREE;

        when(profileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var aCommand = new CreateProfileCommand(expectedUserId, expectedAvatarUrl);

        final var actualProfile = useCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualProfile);
        Assertions.assertNotNull(actualProfile.profileId());
        Assertions.assertEquals(expectedUserId, actualProfile.userId());
        Assertions.assertEquals(expectedAvatarUrl, actualProfile.avatarUrl());
        Assertions.assertEquals(expectedType.name(), actualProfile.type());
    }
}
