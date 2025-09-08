package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.dto.entity.TypeMobileDevice;
import com.betfair.video.api.domain.dto.entity.TypeStream;
import com.betfair.video.api.domain.dto.valueobject.ContentType;
import com.betfair.video.api.domain.dto.valueobject.ExternalId;
import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.dto.valueobject.ServicePermission;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.dto.search.VRAStreamSearchKey;
import com.betfair.video.api.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.exception.InsufficientAccessException;
import com.betfair.video.api.domain.exception.InvalidInputException;
import com.betfair.video.api.domain.exception.StreamNotFoundException;
import com.betfair.video.api.domain.exception.VideoException;
import com.betfair.video.api.domain.mapper.ExternalIdMapper;
import com.betfair.video.api.domain.mapper.TypeStreamMapper;
import com.betfair.video.api.domain.port.input.RetrieveStreamInfoByExternalIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventService implements RetrieveStreamInfoByExternalIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final Integer streamingBrandId;

    private final ExternalIdMapper externalIdMapper;

    private final TypeStreamMapper typeStreamMapper;

    private final StreamService streamService;

    public EventService(Integer streamingBrandId, ExternalIdMapper externalIdMapper, TypeStreamMapper typeStreamMapper, StreamService streamService) {
        this.streamingBrandId = streamingBrandId;
        this.externalIdMapper = externalIdMapper;
        this.typeStreamMapper = typeStreamMapper;
        this.streamService = streamService;
    }

    @Override
    public VideoStreamInfo retrieveScheduleByExternalId(RequestContext context, String externalIdSource, String externalId,
                                                        Integer channelTypeId, List<Integer> channelSubTypeIds, Integer mobileDeviceId,
                                                        String mobileOsVersion, Integer mobileScreenDensityDpi, VideoQuality videoQuality,
                                                        String commentaryLanguage, Integer providerId, ContentType contentType,
                                                        Boolean includeMetadata, String providerParams) {

        if (!context.user().permissions().hasPermission(ServicePermission.VIDEO)) {
            logger.error("[{}]: Access permissions are insufficient for the requested operation or data for user (...)", context.uuid());
            throw new InsufficientAccessException();
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

    private void validateChannelParams(final String uuid, final VRAStreamSearchKey searchKey) throws VideoException {
        if (TypeChannel.NULL.getId().equals(searchKey.getChannelTypeId())) {
            // There are no items with such a channel type in the database
            throw new StreamNotFoundException();
        }

        if (TypeChannel.WEB.getId().equals(searchKey.getChannelTypeId()) && searchKey.getMobileDeviceId() != null) {
            logger.warn("[{}]: Invalid combination of a channel type ({}) and a mobile device ID ({})",
                    uuid, searchKey.getChannelTypeId(), searchKey.getMobileDeviceId());

            final String errorMessage = String.format("Invalid combination of a channel type (%s) and a mobile device ID (%s)", searchKey.getChannelTypeId(), searchKey.getMobileDeviceId());
            throw new InvalidInputException(errorMessage, null);
        }

        if (TypeChannel.MOBILE.getId().equals(searchKey.getChannelTypeId())) {
            boolean isValidDeviceId = TypeMobileDevice.ANDROID_TABLET.getId().equals(searchKey.getMobileDeviceId())
                    || TypeMobileDevice.ANDROID_PHONE.getId().equals(searchKey.getMobileDeviceId())
                    || TypeMobileDevice.IOS_TABLET.getId().equals(searchKey.getMobileDeviceId())
                    || TypeMobileDevice.IOS_PHONE.getId().equals(searchKey.getMobileDeviceId());

            if (!isValidDeviceId) {
                logger.warn("[{}]: Invalid combination of a channel type ({}) and a mobile device ID ({})",
                        uuid, searchKey.getChannelTypeId(), searchKey.getMobileDeviceId());

                final String errorMessage = String.format("Invalid combination of a channel type (%s) and a mobile device ID (%s)", searchKey.getChannelTypeId(), searchKey.getMobileDeviceId());
                throw new InvalidInputException(errorMessage, null);
            }
        }
    }

}
