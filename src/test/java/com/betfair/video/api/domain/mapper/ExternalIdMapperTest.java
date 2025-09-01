package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.domain.valueobject.ExternalId;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExternalIdMapper Tests")
class ExternalIdMapperTest {

    @InjectMocks
    private ExternalIdMapper externalIdMapper;

    @Test
    @DisplayName("Should successfully map valid external IDs")
    void shouldSuccessfullyMapValidExternalIds() {
        // Given
        ExternalIdSource externalIdSource = ExternalIdSource.BETFAIR_EVENT;
        Set<String> externalIds = Set.of("12345");

        // When
        ExternalId externalId = externalIdMapper.map(externalIdSource, externalIds);

        // Then
        assertThat(externalId).isNotNull();
        assertThat(externalId.externalIdSource()).isEqualTo(externalIdSource);
        assertThat(externalId.externalIds()).hasSize(1);
        assertThat(externalId.externalIds().get("12345")).isEmpty();
    }

}
