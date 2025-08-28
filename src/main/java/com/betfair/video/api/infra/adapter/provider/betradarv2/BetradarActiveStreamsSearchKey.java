package com.betfair.video.api.infra.adapter.provider.betradarv2;

public record BetradarActiveStreamsSearchKey(
        int channel,
        int brandId,
        String streamStatusIds,
        String streamProductIds
) {
}
