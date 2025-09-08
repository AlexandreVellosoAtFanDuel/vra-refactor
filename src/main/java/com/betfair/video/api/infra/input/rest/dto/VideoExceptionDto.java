package com.betfair.video.api.infra.input.rest.dto;

import com.betfair.video.api.domain.exception.VideoException;

public record VideoExceptionDto(
        String sportType,
        VideoException.ErrorCodeEnum errorCode
) {
}
