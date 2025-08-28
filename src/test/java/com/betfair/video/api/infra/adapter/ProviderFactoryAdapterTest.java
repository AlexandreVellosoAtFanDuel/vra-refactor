package com.betfair.video.api.infra.adapter;


import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.infra.adapter.provider.betradarv2.BetradarV2ProviderAdapter;
import com.betfair.video.api.infra.adapter.provider.IMGProviderAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProviderFactoryAdapter Tests")
class ProviderFactoryAdapterTest {

    @Mock
    private BetradarV2ProviderAdapter betradarV2ProviderAdapter;

    @Mock
    private IMGProviderAdapter imgProviderAdapter;

    @Test
    @DisplayName("Should create providers map correctly")
    void shouldCreateProvidersMapCorrectly() {
        // When
        ProviderFactoryAdapter providerFactoryAdapter = new ProviderFactoryAdapter(imgProviderAdapter, betradarV2ProviderAdapter);

        // Then - Valid providers
        assertThat(providerFactoryAdapter.getStreamingProviderByIdAndVideoChannelId(26, 1)).isNotNull();
        assertThat(providerFactoryAdapter.getStreamingProviderByIdAndVideoChannelId(33, 1)).isNotNull();

        // Then - Invalid provider
        assertThatThrownBy(() -> providerFactoryAdapter.getStreamingProviderByIdAndVideoChannelId(999999, 1))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.NotFound);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
