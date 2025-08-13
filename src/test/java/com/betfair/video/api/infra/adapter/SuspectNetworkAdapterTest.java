package com.betfair.video.api.infra.adapter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SuspectNetworkAdapter Tests")
class SuspectNetworkAdapterTest {

    private static SuspectNetworkAdapter suspectNetworkAdapter;

    @BeforeAll
    static void setUp() {
        suspectNetworkAdapter = SuspectNetworkAdapter.getInstance();
    }

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
