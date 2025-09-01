package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.dto.ContentTypeDto;
import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.TypeChannel;
import com.betfair.video.api.domain.entity.TypeMobileDevice;
import com.betfair.video.api.domain.entity.TypeStream;
import com.betfair.video.api.domain.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.mapper.ExternalIdMapper;
import com.betfair.video.api.domain.mapper.TypeStreamMapper;
import com.betfair.video.api.domain.valueobject.ExternalId;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.ServicePermission;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.domain.valueobject.search.VRAStreamSearchKey;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Value("${streaming.brand.id}")
    private Integer streamingBrandId;

    private ExternalIdMapper externalIdMapper;

    private TypeStreamMapper typeStreamMapper;

    private StreamService streamService;

    public EventService(ExternalIdMapper externalIdMapper, TypeStreamMapper typeStreamMapper, StreamService streamService) {
        this.externalIdMapper = externalIdMapper;
        this.typeStreamMapper = typeStreamMapper;
        this.streamService = streamService;
    }

    public VideoStreamInfo retrieveScheduleByExternalId(RequestContext context, String externalIdSource, String externalId,
                                                                Integer channelTypeId, List<Integer> channelSubTypeIds, Integer mobileDeviceId,
                                                                String mobileOsVersion, Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                                                String commentaryLanguage, Integer providerId, ContentTypeDto contentType,
                                                                Boolean includeMetadata, String providerParams) {

        if (!context.user().permissions().hasPermission(ServicePermission.VIDEO)) {
            logger.error("[{}]: Access permissions are insufficient for the requested operation or data for user (...)", context.uuid());
            throw new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS, null);
        }

        ExternalIdSource source = ExternalIdSource.fromExternalIdSource(externalIdSource);

        ExternalId parsedExternalIds = externalIdMapper.map(source, Set.of(externalId));

        String primaryId = null;
        String secondaryId = null;

        for (Map.Entry<String, List<String>> entry : parsedExternalIds.externalIds().entrySet()) {
            primaryId = entry.getKey();
            secondaryId = !entry.getValue().isEmpty() ? entry.getValue().getFirst() : null;
        }

        Integer requestedStreamTypeId = typeStreamMapper.convertContentTypeToStreamTypeId(contentType);

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(parsedExternalIds.externalIdSource())
                .primaryId(primaryId)
                .secondaryId(secondaryId)
                .channelTypeId(channelTypeId)
                .channelSubTypeIds(channelSubTypeIds)
                .mobileDeviceId(mobileDeviceId)
                .mobileOsVersion(mobileOsVersion)
                .mobileScreenDensityDpi(mobileScreenDensityDpi)
                .videoQuality(videoQuality)
                .commentaryLanguage(commentaryLanguage)
                .providerId(providerId)
                .contentType(contentType)
                .streamTypeIds(requestedStreamTypeId != null ? Collections.singleton(requestedStreamTypeId) : TypeStream.REGULAR_STREAM_TYPES)
                .brandIds(Collections.singleton(streamingBrandId))
                .providerParams(providerParams)
                .build();

        validateChannelParams(context.uuid(), searchKey);

        return streamService.getStreamInfoByExternalId(searchKey, context, Boolean.TRUE.equals(includeMetadata));
    }

    private void validateChannelParams(final String uuid, final VRAStreamSearchKey searchKey) throws VideoAPIException {
        if (TypeChannel.NULL.getId().equals(searchKey.getChannelTypeId())) {
            // There are no items with such a channel type in the database
            throw new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, null);
        }

        if (TypeChannel.WEB.getId().equals(searchKey.getChannelTypeId()) && searchKey.getMobileDeviceId() != null) {
            logger.warn("[{}]: Invalid combination of a channel type ({}) and a mobile device ID ({})",
                    uuid, searchKey.getChannelTypeId(), searchKey.getMobileDeviceId());
            throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
        }

        if (TypeChannel.MOBILE.getId().equals(searchKey.getChannelTypeId())) {
            boolean isValidDeviceId = TypeMobileDevice.ANDROID_TABLET.getId().equals(searchKey.getMobileDeviceId())
                    || TypeMobileDevice.ANDROID_PHONE.getId().equals(searchKey.getMobileDeviceId())
                    || TypeMobileDevice.IOS_TABLET.getId().equals(searchKey.getMobileDeviceId())
                    || TypeMobileDevice.IOS_PHONE.getId().equals(searchKey.getMobileDeviceId());

            if (!isValidDeviceId) {
                logger.warn("[{}]: Invalid combination of a channel type ({}) and a mobile device ID ({})",
                        uuid, searchKey.getChannelTypeId(), searchKey.getMobileDeviceId());
                throw new VideoAPIException(ResponseCode.BadRequest, VideoAPIExceptionErrorCodeEnum.INVALID_INPUT, null);
            }
        }
    }

}
