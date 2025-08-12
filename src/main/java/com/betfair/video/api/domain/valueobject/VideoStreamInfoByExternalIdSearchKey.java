package com.betfair.video.api.domain.valueobject;

import com.betfair.video.api.application.dto.ContentTypeDto;

import java.util.List;
import java.util.Set;

public class VideoStreamInfoByExternalIdSearchKey {

    private final Integer channelTypeId;
    private final List<Integer> channelSubTypeIds;
    private final Integer mobileDeviceId;
    private final String mobileOsVersion;
    private final Integer mobileScreenDensityDpi;
    private final VideoQuality videoQuality;
    private final String commentaryLanguage;
    private final Integer providerId;
    private final ContentTypeDto contentType;
    private final Set<Integer> streamTypeIds;
    private final Set<Integer> brandIds;
    private final String providerParams;
    private final ExternalIdSource externalIdSource;
    private final String primaryId;
    private final String secondaryId;

    private VideoStreamInfoByExternalIdSearchKey(Builder builder) {
        this.channelTypeId = builder.channelTypeId;
        this.channelSubTypeIds = builder.channelSubTypeIds;
        this.mobileDeviceId = builder.mobileDeviceId;
        this.mobileOsVersion = builder.mobileOsVersion;
        this.mobileScreenDensityDpi = builder.mobileScreenDensityDpi;
        this.videoQuality = builder.videoQuality;
        this.commentaryLanguage = builder.commentaryLanguage;
        this.providerId = builder.providerId;
        this.contentType = builder.contentType;
        this.streamTypeIds = builder.streamTypeIds;
        this.brandIds = builder.brandIds;
        this.providerParams = builder.providerParams;
        this.externalIdSource = builder.externalIdSource;
        this.primaryId = builder.primaryId;
        this.secondaryId = builder.secondaryId;
    }

    public Integer getChannelTypeId() {
        return channelTypeId;
    }

    public List<Integer> getChannelSubTypeIds() {
        return channelSubTypeIds;
    }

    public Integer getMobileDeviceId() {
        return mobileDeviceId;
    }

    public String getMobileOsVersion() {
        return mobileOsVersion;
    }

    public Integer getMobileScreenDensityDpi() {
        return mobileScreenDensityDpi;
    }

    public VideoQuality getVideoQuality() {
        return videoQuality;
    }

    public String getCommentaryLanguage() {
        return commentaryLanguage;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public ContentTypeDto getContentType() {
        return contentType;
    }

    public Set<Integer> getStreamTypeIds() {
        return streamTypeIds;
    }

    public Set<Integer> getBrandIds() {
        return brandIds;
    }

    public String getProviderParams() {
        return providerParams;
    }

    public ExternalIdSource getExternalIdSource() {
        return externalIdSource;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer channelTypeId;
        private List<Integer> channelSubTypeIds;
        private Integer mobileDeviceId;
        private String mobileOsVersion;
        private Integer mobileScreenDensityDpi;
        private VideoQuality videoQuality;
        private String commentaryLanguage;
        private Integer providerId;
        private ContentTypeDto contentType;
        private Set<Integer> streamTypeIds;
        private Set<Integer> brandIds;
        private String providerParams;
        private ExternalIdSource externalIdSource;
        private String primaryId;
        private String secondaryId;

        public Builder() {
        }

        public Builder channelTypeId(Integer channelTypeId) {
            this.channelTypeId = channelTypeId;
            return this;
        }

        public Builder channelSubTypeIds(List<Integer> channelSubTypeIds) {
            this.channelSubTypeIds = channelSubTypeIds;
            return this;
        }

        public Builder mobileDeviceId(Integer mobileDeviceId) {
            this.mobileDeviceId = mobileDeviceId;
            return this;
        }

        public Builder mobileOsVersion(String mobileOsVersion) {
            this.mobileOsVersion = mobileOsVersion;
            return this;
        }

        public Builder mobileScreenDensityDpi(Integer mobileScreenDensityDpi) {
            this.mobileScreenDensityDpi = mobileScreenDensityDpi;
            return this;
        }

        public Builder videoQuality(VideoQuality videoQuality) {
            this.videoQuality = videoQuality;
            return this;
        }

        public Builder commentaryLanguage(String commentaryLanguage) {
            this.commentaryLanguage = commentaryLanguage;
            return this;
        }

        public Builder providerId(Integer providerId) {
            this.providerId = providerId;
            return this;
        }

        public Builder contentType(ContentTypeDto contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder streamTypeIds(Set<Integer> streamTypeIds) {
            this.streamTypeIds = streamTypeIds;
            return this;
        }

        public Builder brandIds(Set<Integer> brandIds) {
            this.brandIds = brandIds;
            return this;
        }

        public Builder providerParams(String providerParams) {
            this.providerParams = providerParams;
            return this;
        }

        public Builder externalIdSource(ExternalIdSource externalIdSource) {
            this.externalIdSource = externalIdSource;
            return this;
        }

        public Builder primaryId(String primaryId) {
            this.primaryId = primaryId;
            return this;
        }

        public Builder secondaryId(String secondaryId) {
            this.secondaryId = secondaryId;
            return this;
        }

        public VideoStreamInfoByExternalIdSearchKey build() {
            return new VideoStreamInfoByExternalIdSearchKey(this);
        }
    }
}
