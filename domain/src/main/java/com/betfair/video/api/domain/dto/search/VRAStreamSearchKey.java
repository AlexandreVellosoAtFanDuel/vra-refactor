package com.betfair.video.api.domain.dto.search;

import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;

import java.util.List;
import java.util.Set;

public class VRAStreamSearchKey {

    protected Integer channelTypeId;
    protected List<Integer> channelSubTypeIds;
    protected Integer mobileDeviceId;
    protected String mobileOsVersion;
    protected Integer mobileScreenDensityDpi;
    protected VideoQuality videoQuality;
    protected String commentaryLanguage;
    protected Integer providerId;
    protected ContentType contentType;
    protected Set<Integer> streamTypeIds;
    protected Set<Integer> brandIds;
    protected String providerParams;

    public VRAStreamSearchKey(Integer channelTypeId, List<Integer> channelSubTypeIds, Integer mobileDeviceId,
                              String mobileOsVersion, Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                              String commentaryLanguage, Integer providerId, ContentType contentType,
                              Set<Integer> streamTypeIds, Set<Integer> brandIds, String providerParams) {
        this.channelTypeId = channelTypeId;
        this.channelSubTypeIds = channelSubTypeIds;
        this.mobileDeviceId = mobileDeviceId;
        this.mobileOsVersion = mobileOsVersion;
        this.mobileScreenDensityDpi = mobileScreenDensityDpi;
        this.videoQuality = videoQuality;
        this.commentaryLanguage = commentaryLanguage;
        this.providerId = providerId;
        this.contentType = contentType;
        this.streamTypeIds = streamTypeIds;
        this.brandIds = brandIds;
        this.providerParams = providerParams;
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

    public ContentType getContentType() {
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

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public void setStreamTypeIds(Set<Integer> streamTypeIds) {
        this.streamTypeIds = streamTypeIds;
    }

    public static class Builder {
        protected Integer channelTypeId;
        protected List<Integer> channelSubTypeIds;
        protected Integer mobileDeviceId;
        protected String mobileOsVersion;
        protected Integer mobileScreenDensityDpi;
        protected VideoQuality videoQuality;
        protected String commentaryLanguage;
        protected Integer providerId;
        protected ContentType contentType;
        protected Set<Integer> streamTypeIds;
        protected Set<Integer> brandIds;
        protected String providerParams;

        protected Builder() {
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

        public Builder contentType(ContentType contentType) {
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

        public VRAStreamSearchKey build() {
            return new VRAStreamSearchKey(channelTypeId, channelSubTypeIds, mobileDeviceId, mobileOsVersion,
                    mobileScreenDensityDpi, videoQuality, commentaryLanguage, providerId, contentType,
                    streamTypeIds, brandIds, providerParams);
        }
    }
}
