package adapter.provider;

import com.betfair.video.output.adapter.provider.IMGProviderAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImgProviderAdapter Tests")
class ImgProviderAdapterTest {

    @InjectMocks
    IMGProviderAdapter imgProvider;

    @Test
    @DisplayName("Test adapter.provider is enabled")
    void testProviderIsEnabled() {
        // Given
        ReflectionTestUtils.setField(imgProvider, "isEnabled", "true");

        // When
        boolean isEnabled = imgProvider.isEnabled();

        // Then
        assertThat(isEnabled).isTrue();
    }

    @Test
    @DisplayName("Test adapter.provider is not enabled")
    void testProviderIsNotEnabled() {
        // Given
        ReflectionTestUtils.setField(imgProvider, "isEnabled", "false");

        // When
        boolean isEnabled = imgProvider.isEnabled();

        // Then
        assertThat(isEnabled).isFalse();
    }

}
