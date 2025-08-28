package com.betfair.video.api.infra.adapter.provider;

import com.betfair.video.api.infra.adapter.provider.betradarv2.BetradarV2ProviderAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("BetradarV2ProviderAdapter Tests")
class BetradarV2ProviderAdapterTest {

    @InjectMocks
    BetradarV2ProviderAdapter betradarV2Provider;

    @Test
    @DisplayName("Test provider is enabled")
    void testProviderIsEnabled() {
        // Given
        ReflectionTestUtils.setField(betradarV2Provider, "isEnabled", "true");

        // When
        boolean isEnabled = betradarV2Provider.isEnabled();

        // Then
        assertThat(isEnabled).isTrue();
    }

    @Test
    @DisplayName("Test provider is not enabled")
    void testProviderIsNotEnabled() {
        // Given
        ReflectionTestUtils.setField(betradarV2Provider, "isEnabled", "false");

        // When
        boolean isEnabled = betradarV2Provider.isEnabled();

        // Then
        assertThat(isEnabled).isFalse();
    }

}
