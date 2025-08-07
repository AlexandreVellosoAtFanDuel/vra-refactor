package com.betfair.video.api.application.dto;

import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;

public record VideoApiExceptionDto(
        String sportType,
        VideoAPIExceptionErrorCodeEnum errorCode
) {
}
