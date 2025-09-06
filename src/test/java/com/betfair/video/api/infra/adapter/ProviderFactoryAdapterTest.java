package com.betfair.video.api.infra.adapter;


import com.betfair.video.api.infra.output.adapter.ProviderFactoryAdapter;
import com.betfair.video.api.infra.output.adapter.provider.BetradarV2Adapter;
import com.betfair.video.api.infra.output.adapter.provider.IMGProviderAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProviderFactoryAdapter Tests")
class ProviderFactoryAdapterTest {

    @Mock
    private BetradarV2Adapter betradarV2ProviderAdapter;

    @Mock
    private IMGProviderAdapter imgProviderAdapter;

    @Test
    @DisplayName("Should create providers map correctly")
    void shouldCreateProvidersMapCorrectly() {
        // When
        ProviderFactoryAdapter providerFactoryAdapter = new ProviderFactoryAdapter(imgProviderAdapter, betradarV2ProviderAdapter);

        // Then - Valid providers
        assertThat(providerFactoryAdapter.getStreamingProviderByIdAndVideoChannelId(26)).isNotNull();
        assertThat(providerFactoryAdapter.getStreamingProviderByIdAndVideoChannelId(33)).isNotNull();

        // Then - Invalid provider
        assertThat(providerFactoryAdapter.getStreamingProviderByIdAndVideoChannelId(999999)).isNull();
    }

}
