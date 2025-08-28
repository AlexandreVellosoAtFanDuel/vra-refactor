package com.betfair.video.api.domain.valueobject;

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
