package adapter;

import com.betfair.video.api.output.adapter.SuspectNetworkAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("SuspectNetworkAdapter Tests")
class SuspectNetworkAdapterTest {

    @InjectMocks
    private SuspectNetworkAdapter suspectNetworkAdapter;

    @Test
    @DisplayName("Should return false for non-suspect network")
    void shouldReturnFalseForNonSuspectNetwork() {
        // Given
        ReflectionTestUtils.setField(suspectNetworkAdapter, "suspectNetworksConfig", "");

        boolean isSuspect = suspectNetworkAdapter.isSuspectNetwork("192.168.0.1");

        assertThat(isSuspect).isFalse();
    }

    @Test
    @DisplayName("Should return true for suspect network")
    void shouldReturnTrueForSuspectNetwork() {
        // Given
        ReflectionTestUtils.setField(suspectNetworkAdapter, "suspectNetworksConfig", "192.168.0.10");

        // When
        boolean isSuspect = suspectNetworkAdapter.isSuspectNetwork("192.168.0.10");

        // Then
        assertThat(isSuspect).isTrue();
    }

}
