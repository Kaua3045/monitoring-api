package com.kaua.monitoring.application.usecases.profile.delete;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.domain.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProfileUseCaseTest {

    @InjectMocks
    private DefaultDeleteProfileUseCase useCase;

    @Mock
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidProfileId_whenCallsDeleteById_shouldBeOk() {
        final var aProfile = Profile.newProfile(
                "1234567",
                "a",
                "a@mail.com",
                null);
        final var expectedProfileId = aProfile.getId().getValue();

        doNothing()
                .when(profileGateway).deleteById(expectedProfileId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteProfileCommand(expectedProfileId)));

        Mockito.verify(profileGateway, times(1)).deleteById(expectedProfileId);
    }

    @Test
    public void givenAnInvalidProfileId_whenCallsDeleteById_shouldBeOk() {
        final var expectedProfileId = "123";

        doNothing()
                .when(profileGateway).deleteById(expectedProfileId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(new DeleteProfileCommand(expectedProfileId)));

        Mockito.verify(profileGateway, times(1)).deleteById(expectedProfileId);
    }
}
