package com.betfair.video.api.domain.mapper;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.valueobject.ExternalId;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExternalIdMapper Tests")
class ExternalIdMapperTest {

    @InjectMocks
    private ExternalIdMapper externalIdMapper;

    @Test
    @DisplayName("Should successfully map valid external IDs")
    void shouldSuccessfullyMapValidExternalIds() {
        // Given
        RequestContext context = mock(RequestContext.class);

        ExternalIdSource externalIdSource = ExternalIdSource.BETFAIR_EVENT;
        Set<String> externalIds = Set.of("12345");

        // When
        ExternalId externalId = externalIdMapper.map(context, externalIdSource, externalIds);

        // Then
        assertThat(externalId).isNotNull();
        assertThat(externalId.externalIdSource()).isEqualTo(externalIdSource);
        assertThat(externalId.externalIds()).hasSize(1);
        assertThat(externalId.externalIds().get("12345")).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception for invalid external ID source")
    void shouldThrowExceptionForInvalidExternalIdSource() {
        // Given
        RequestContext context = mock(RequestContext.class);

        // BETFAIR_VIDEO is not a valid source for mapping
        ExternalIdSource externalIdSource = ExternalIdSource.BETFAIR_VIDEO;
        Set<String> externalIds = Set.of("12345");

        // When & Then
        assertThatThrownBy(() -> externalIdMapper.map(context, externalIdSource, externalIds))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoAPIException = (VideoAPIException) exception;
                    assertThat(videoAPIException.getResponseCode()).isEqualTo(ResponseCode.BadRequest);
                    assertThat(videoAPIException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INVALID_INPUT);
                    assertThat(videoAPIException.getSportType()).isNull();
                });
    }

}
