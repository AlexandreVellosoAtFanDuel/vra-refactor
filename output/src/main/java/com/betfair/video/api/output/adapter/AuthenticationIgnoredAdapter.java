package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.port.output.AuthenticationIgnoredPort;
import com.betfair.video.api.output.util.ConfigFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
            authenticationIgnoredMap = buildProviderMap(authenticationIgnoredWeb, authenticationIgnoredMobile);
        }

        return authenticationIgnoredMap.containsKey(videoChannelType) && authenticationIgnoredMap.get(videoChannelType).contains(providerId);
    }

    private Map<Integer, Set<Integer>> buildProviderMap(String webProviders, String mobileProviders) {
        Set<Integer> webSet = ConfigFormatter.csvToIntegerSet(webProviders);
        Set<Integer> mobileSet = ConfigFormatter.csvToIntegerSet(mobileProviders);

        return Map.of(TypeChannel.WEB.getId(), webSet, TypeChannel.MOBILE.getId(), mobileSet);
    }

}
