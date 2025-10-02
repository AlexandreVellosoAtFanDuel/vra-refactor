package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.port.output.DirectStreamConfigPort;
import com.betfair.video.api.output.util.ConfigFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class DirectStreamConfigAdapter implements DirectStreamConfigPort {

    @Value("${stream.direct.web}")
    private String streamDirectWeb;

    @Value("${stream.direct.mobile}")
    private String streamDirectMobile;

    private Map<Integer, Set<Integer>> streamDirectMap = null;

    @Override
    public boolean isProviderInList(Integer providerId, Integer channelTypeId) {
        if (streamDirectMap == null) {
            streamDirectMap = buildStreamDirectMap(streamDirectWeb, streamDirectMobile);
        }

        return streamDirectMap.containsKey(channelTypeId) && streamDirectMap.get(channelTypeId).contains(providerId);
    }

    private Map<Integer, Set<Integer>> buildStreamDirectMap(String web, String mobile) {
        Set<Integer> webSet = ConfigFormatter.csvToIntegerSet(web);
        Set<Integer> mobileSet = ConfigFormatter.csvToIntegerSet(mobile);

        return Map.of(TypeChannel.WEB.getId(), webSet, TypeChannel.MOBILE.getId(), mobileSet);
    }

}
