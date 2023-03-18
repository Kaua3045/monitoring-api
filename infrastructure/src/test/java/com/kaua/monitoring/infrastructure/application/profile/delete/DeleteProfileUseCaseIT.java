package com.kaua.monitoring.infrastructure.application.profile.delete;

import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileCommand;
import com.kaua.monitoring.application.usecases.profile.delete.DeleteProfileUseCase;
import com.kaua.monitoring.domain.profile.Profile;
import com.kaua.monitoring.domain.profile.ProfileID;
import com.kaua.monitoring.infrastructure.IntegrationTest;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileJpaFactory;
import com.kaua.monitoring.infrastructure.profile.persistence.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteProfileUseCaseIT {

    @Autowired
    private DeleteProfileUseCase deleteProfileUseCase;

    @Autowired
    private ProfileRepository profileRepository;

    @SpyBean
    private ProfileGateway profileGateway;

    @Test
    public void givenAnValidId_whenCallsDeleteProfile_shouldBeOK() {
        final var aProfile = Profile.newProfile(
                "123",
                "kaua",
                "kaua@teste.com",
                null
        );
        profileRepository.save(ProfileJpaFactory.toEntity(aProfile));

        final var expectedId = aProfile.getId().getValue();

        Assertions.assertEquals(1, profileRepository.count());

        final var aCommand = new DeleteProfileCommand(expectedId);

        Assertions.assertDoesNotThrow(() -> deleteProfileUseCase.execute(aCommand));

        Assertions.assertEquals(0, profileRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteProfile_shouldBeOK() {
        final var expectedId = ProfileID.from("123");

        Assertions.assertEquals(0, profileRepository.count());

        final var aCommand = new DeleteProfileCommand(expectedId.getValue());

        Assertions.assertDoesNotThrow(() -> deleteProfileUseCase.execute(aCommand));

        Assertions.assertEquals(0, profileRepository.count());
    }
}
