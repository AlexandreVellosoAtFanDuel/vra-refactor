package com.betfair.video.api.domain.valueobject;

public enum ReferenceTypeId {

    VIDEO_PROVIDER("VIDEO_PROVIDER", TranslationEntityType.REFERENCE_TYPE_PROVIDER),
    SPORTS_TYPE("SPORTS_TYPE", TranslationEntityType.REFERENCE_TYPE_SPORT),
    CHANNEL_TYPE("CHANNEL_TYPE", TranslationEntityType.REFERENCE_TYPE_CHANNEL),
    CHANNEL_SUB_TYPE("CHANNEL_SUB_TYPE", TranslationEntityType.REFERENCE_TYPE_CHANNEL_SUB_TYPE),
    EXTERNAL_ID_PROVIDER("EXTERNAL_ID_PROVIDER", (TranslationEntityType)null),
    VIDEO_FORMAT("VIDEO_FORMAT", (TranslationEntityType)null),
    COUNTRY("COUNTRY", TranslationEntityType.REFERENCE_TYPE_COUNTRY),
    COUNTRY_STATE("COUNTRY_STATE", TranslationEntityType.REFERENCE_TYPE_COUNTRY_STATE),
    MOBILE_DEVICE_ID("MOBILE_DEVICE_ID", TranslationEntityType.REFERENCE_TYPE_MOBILE_DEVICE_ID),
    STREAM_TYPE("STREAM_TYPE", TranslationEntityType.REFERENCE_TYPE_STREAM_TYPE),
    BRAND("BRAND", TranslationEntityType.REFERENCE_TYPE_BRAND),
    AUDIT_SYSTEM("AUDIT_SYSTEM", (TranslationEntityType)null),
    UNRECOGNIZED_VALUE((String)null, (TranslationEntityType)null);

    private String value;
    private TranslationEntityType translationEntityType;

    ReferenceTypeId(String value, TranslationEntityType translationEntityType) {
        this.value = value;
        this.translationEntityType = translationEntityType;
    }
}
