package com.betfair.video.domain.dto.valueobject;

public record SizeRestrictions(
        Integer widthPercentage,
        Integer heightPercentage,
        Integer widthPixel,
        Integer heightPixel,
        Integer widthCentimeter,
        Integer heightCentimeter,
        Boolean fullScreenAllowed,
        Boolean airPlayAllowed,
        String aspectRatio,
        Integer widthMax,
        Integer widthDefault
) {
}
