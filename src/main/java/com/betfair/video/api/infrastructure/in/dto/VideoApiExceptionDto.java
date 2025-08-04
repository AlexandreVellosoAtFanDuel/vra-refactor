package com.betfair.video.api.infrastructure.in.dto;

import com.betfair.video.api.infrastructure.in.exception.VideoAPIExceptionErrorCodeEnum;

public record VideoApiExceptionDto(
        String sportType,
        VideoAPIExceptionErrorCodeEnum errorCode
) {
}
