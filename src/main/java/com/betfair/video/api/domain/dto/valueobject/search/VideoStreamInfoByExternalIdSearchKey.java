package com.betfair.video.api.domain.dto.valueobject.search;

import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.infra.input.rest.dto.ContentTypeDto;

import java.util.List;
import java.util.Set;

public class VideoStreamInfoByExternalIdSearchKey extends VRAStreamSearchKey {

    private ExternalIdSource externalIdSource;
    private String primaryId;
    private String secondaryId;

    public VideoStreamInfoByExternalIdSearchKey(Integer channelTypeId, List<Integer> channelSubTypeIds,
                                               Integer mobileDeviceId, String mobileOsVersion, 
                                               Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                               String commentaryLanguage, Integer providerId, 
                                               ContentTypeDto contentType, Set<Integer> streamTypeIds, 
                                               Set<Integer> brandIds, String providerParams,
                                               ExternalIdSource externalIdSource, String primaryId, 
                                               String secondaryId) {
        super(channelTypeId, channelSubTypeIds, mobileDeviceId, mobileOsVersion, mobileScreenDensityDpi,
              videoQuality, commentaryLanguage, providerId, contentType, streamTypeIds, brandIds, providerParams);
        this.externalIdSource = externalIdSource;
        this.primaryId = primaryId;
        this.secondaryId = secondaryId;
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

    public void setExternalIdSource(ExternalIdSource externalIdSource) {
        this.externalIdSource = externalIdSource;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public static class Builder extends VRAStreamSearchKey.Builder {
        private ExternalIdSource externalIdSource;
        private String primaryId;
        private String secondaryId;

        public Builder() {
            super();
        }

        @Override
        public Builder channelTypeId(Integer channelTypeId) {
            super.channelTypeId(channelTypeId);
            return this;
        }

        @Override
        public Builder channelSubTypeIds(List<Integer> channelSubTypeIds) {
            super.channelSubTypeIds(channelSubTypeIds);
            return this;
        }

        @Override
        public Builder mobileDeviceId(Integer mobileDeviceId) {
            super.mobileDeviceId(mobileDeviceId);
            return this;
        }

        @Override
        public Builder mobileOsVersion(String mobileOsVersion) {
            super.mobileOsVersion(mobileOsVersion);
            return this;
        }

        @Override
        public Builder mobileScreenDensityDpi(Integer mobileScreenDensityDpi) {
            super.mobileScreenDensityDpi(mobileScreenDensityDpi);
            return this;
        }

        @Override
        public Builder videoQuality(VideoQuality videoQuality) {
            super.videoQuality(videoQuality);
            return this;
        }

        @Override
        public Builder commentaryLanguage(String commentaryLanguage) {
            super.commentaryLanguage(commentaryLanguage);
            return this;
        }

        @Override
        public Builder providerId(Integer providerId) {
            super.providerId(providerId);
            return this;
        }

        @Override
        public Builder contentType(ContentTypeDto contentType) {
            super.contentType(contentType);
            return this;
        }

        @Override
        public Builder streamTypeIds(Set<Integer> streamTypeIds) {
            super.streamTypeIds(streamTypeIds);
            return this;
        }

        @Override
        public Builder brandIds(Set<Integer> brandIds) {
            super.brandIds(brandIds);
            return this;
        }

        @Override
        public Builder providerParams(String providerParams) {
            super.providerParams(providerParams);
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

        @Override
        public VideoStreamInfoByExternalIdSearchKey build() {
            return new VideoStreamInfoByExternalIdSearchKey(
                channelTypeId, channelSubTypeIds, mobileDeviceId, mobileOsVersion,
                mobileScreenDensityDpi, videoQuality, commentaryLanguage, providerId,
                contentType, streamTypeIds, brandIds, providerParams,
                externalIdSource, primaryId, secondaryId
            );
        }
    }
}
