package com.betfair.video.api.infra.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SuspectNetworkAdapter.class})
@DisplayName("SuspectNetworkAdapter Tests")
@TestPropertySource(properties = {
        "geoip.suspect-networks=192.168.0.10",
})
class SuspectNetworkAdapterTest {

    @Autowired
    private SuspectNetworkAdapter suspectNetworkAdapter;

    @Test
    @DisplayName("Should return false for non-suspect network")
    void shouldReturnFalseForNonSuspectNetwork() {
        boolean isSuspect = suspectNetworkAdapter.isSuspectNetwork("192.168.0.1");

        assertThat(isSuspect).isFalse();
    }

    @Test
    @DisplayName("Should return true for suspect network")
    void shouldReturnTrueForSuspectNetwork() {
        boolean isSuspect = suspectNetworkAdapter.isSuspectNetwork("192.168.0.10");

        assertThat(isSuspect).isTrue();
    }

}
