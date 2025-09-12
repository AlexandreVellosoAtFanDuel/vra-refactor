package com.betfair.video.output.adapter;

import com.betfair.video.domain.dto.valueobject.NetworkAddress;
import com.betfair.video.domain.port.output.SuspectNetworkPort;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SuspectNetworkAdapter implements SuspectNetworkPort {

    @Value("${geoip.suspect-networks}")
    private String suspectNetworksConfig;
    private Set<NetworkAddress> suspectNetworks;

    @Override
    public boolean isSuspectNetwork(String ipAddress) {
        if (suspectNetworks == null) {
            suspectNetworks = buildSuspectNetworks();
        }

        return suspectNetworks.stream()
                .anyMatch(network -> network.ip().equals(ipAddress));
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

}
