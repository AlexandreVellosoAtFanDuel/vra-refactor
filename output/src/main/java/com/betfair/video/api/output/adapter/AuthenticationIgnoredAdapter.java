package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.port.output.AuthenticationIgnoredPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class AuthenticationIgnoredAdapter implements AuthenticationIgnoredPort {

    @Value("${authentication.ignored.web}")
    private String authenticationIgnoredWeb;

    @Value("${authentication.ignored.mobile}")
    private String authenticationIgnoredMobile;

    private Map<Integer, Set<Integer>> authenticationIgnoredMap = null;

    @Override
    public boolean isProviderInList(Integer providerId, Integer videoChannelType) {
        if (authenticationIgnoredMap == null) {
            authenticationIgnoredMap = buildProviderSet(authenticationIgnoredWeb, authenticationIgnoredMobile);
        }

        return authenticationIgnoredMap.containsKey(videoChannelType) && authenticationIgnoredMap.get(videoChannelType).contains(providerId);
    }

    private Map<Integer, Set<Integer>> buildProviderSet(String webProviders, String mobileProviders) {
        Set<Integer> webSet = providersToSet(webProviders);
        Set<Integer> mobileSet = providersToSet(mobileProviders);

        return Map.of(TypeChannel.WEB.getId(), webSet, TypeChannel.MOBILE.getId(), mobileSet);
    }

    private Set<Integer> providersToSet(String providers) {
        String[] providersArray = providers.split(",");

        Set<Integer> set = new HashSet<>();
        for (String provider : providersArray) {
            set.add(Integer.parseInt(provider.trim()));
        }

        return set;
    }

}
