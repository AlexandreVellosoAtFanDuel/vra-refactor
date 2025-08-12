package com.betfair.video.api.domain.valueobject;

public record SizeRestrictions(
        int widthPercentage,
        int heightPercentage,
        int widthPixel,
        int heightPixel,
        int widthCentimeter,
        int heightCentimeter,
        boolean fullScreenAllowed,
        boolean airPlayAllowed,
        String aspectRatio,
        int widthMax,
        int widthDefault
) {
}
