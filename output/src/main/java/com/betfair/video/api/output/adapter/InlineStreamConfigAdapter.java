package com.betfair.video.api.output.adapter;

import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.port.output.InlineStreamConfigPort;
import com.betfair.video.api.output.util.ConfigFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class InlineStreamConfigAdapter implements InlineStreamConfigPort {

    @Value("${stream.inline.mobile}")
    private String streamInlineMobile;

    private Map<Integer, Set<Integer>> streamInlineMap = null;

    @Override
    public boolean isProviderInList(Integer providerId, Integer channelTypeId) {
        if (streamInlineMap == null) {
            streamInlineMap = buildStreamInlineMap(streamInlineMobile);
        }

        return streamInlineMap.containsKey(channelTypeId) && streamInlineMap.get(channelTypeId).contains(providerId);
    }

    private Map<Integer, Set<Integer>> buildStreamInlineMap(String mobile) {
        Set<Integer> mobileSet = ConfigFormatter.csvToIntegerSet(mobile);

        return Map.of(TypeChannel.MOBILE.getId(), mobileSet);
    }

}
