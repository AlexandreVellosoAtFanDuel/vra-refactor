package com.betfair.video.api.infra.input.rest.dto;

import com.betfair.video.api.infra.input.rest.exception.VideoAPIExceptionErrorCodeEnum;

public record VideoApiExceptionDto(
        String sportType,
        VideoAPIExceptionErrorCodeEnum errorCode
) {
}
