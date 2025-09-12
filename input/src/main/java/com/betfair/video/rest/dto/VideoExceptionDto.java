package com.betfair.video.rest.dto;

import com.betfair.video.domain.exception.VideoException;

public record VideoExceptionDto(
        String sportType,
        VideoException.ErrorCodeEnum errorCode
) {
}
