package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.valueobject.NetworkAddress;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class SuspectNetworkAdapter {

    private String suspectNetworksConfig;
    private Set<NetworkAddress> suspectNetworks;
    private static volatile SuspectNetworkAdapter instance;

    public static SuspectNetworkAdapter getInstance() {
        if (instance == null) {
            synchronized (SuspectNetworkAdapter.class) {
                if (instance == null) {
                    instance = new SuspectNetworkAdapter();
                }
            }
        }
        return instance;
    }

    private SuspectNetworkAdapter() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                this.suspectNetworksConfig = properties.getProperty("geoip.suspect-networks", "");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    private Set<NetworkAddress> buildSuspectNetworks() {
        if (Strings.isEmpty(suspectNetworksConfig)) {
            return new HashSet<>();
        }

        String[] networks = suspectNetworksConfig.split(",");

        Set<NetworkAddress> suspected = new HashSet<>();
        for (String network : networks) {
            suspected.add(new NetworkAddress(network.trim()));
        }

        return suspected;
    }

    public boolean isSuspectNetwork(String ipAddress) {
        if (suspectNetworks == null) {
            suspectNetworks = buildSuspectNetworks();
        }

        return suspectNetworks.stream()
                .anyMatch(network -> network.ip().equals(ipAddress));
    }

}
