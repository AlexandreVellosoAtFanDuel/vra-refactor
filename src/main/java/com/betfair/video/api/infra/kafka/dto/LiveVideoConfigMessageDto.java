package com.betfair.video.api.infra.kafka.dto;

import java.util.List;
import java.util.Map;

public record LiveVideoConfigMessageDto(
        Long msgId,
        Long publishTime,
        List<DbConfigDto> dbConfigs,
        Map<String, List<SportItemDto>> referenceTypes
) {
}
