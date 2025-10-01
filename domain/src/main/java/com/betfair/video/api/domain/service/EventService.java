package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.TypeChannel;
import com.betfair.video.api.domain.dto.entity.TypeMobileDevice;
import com.betfair.video.api.domain.dto.entity.TypeStream;
import com.betfair.video.api.domain.dto.search.VRAStreamSearchKey;
import com.betfair.video.api.domain.dto.search.VideoStreamInfoByExternalIdSearchKey;
import com.betfair.video.api.domain.dto.valueobject.ExternalId;
import com.betfair.video.api.domain.dto.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.dto.valueobject.RetrieveScheduleByExternalIdParams;
import com.betfair.video.api.domain.dto.valueobject.ServicePermission;
import com.betfair.video.api.domain.dto.valueobject.VideoStreamInfo;
import com.betfair.video.api.domain.exception.InsufficientAccessException;
import com.betfair.video.api.domain.exception.InvalidInputException;
import com.betfair.video.api.domain.exception.StreamNotFoundException;
import com.betfair.video.api.domain.exception.VideoException;
import com.betfair.video.api.domain.mapper.ExternalIdMapper;
import com.betfair.video.api.domain.mapper.TypeStreamMapper;
import com.betfair.video.api.domain.port.input.EventServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EventService implements EventServicePort {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Value("${streaming.brand.id}")
    private Integer streamingBrandId;

    private final StreamService streamService;

    public EventService(StreamService streamService) {
        this.streamService = streamService;
    }

    @Override
    public VideoStreamInfo retrieveScheduleByExternalId(RetrieveScheduleByExternalIdParams params) {
        RequestContext context = params.context();

        if (context.user() == null || !context.user().permissions().hasPermission(ServicePermission.VIDEO)) {
            logger.error("[{}]: Access permissions are insufficient for the requested operation or data for user (...)", context.uuid());
            throw new InsufficientAccessException();
        }

        ExternalIdSource source = ExternalIdSource.fromExternalIdSource(params.externalIdSource());

        ExternalId parsedExternalIds = ExternalIdMapper.map(source, Set.of(params.externalId()));

        String primaryId = null;
        String secondaryId = null;

        for (Map.Entry<String, List<String>> entry : parsedExternalIds.externalIds().entrySet()) {
            primaryId = entry.getKey();
            secondaryId = !entry.getValue().isEmpty() ? entry.getValue().getFirst() : null;
        }

        Integer requestedStreamTypeId = TypeStreamMapper.convertContentTypeToStreamTypeId(params.contentType());

        VideoStreamInfoByExternalIdSearchKey searchKey = new VideoStreamInfoByExternalIdSearchKey.Builder()
                .externalIdSource(parsedExternalIds.externalIdSource())
                .primaryId(primaryId)
                .secondaryId(secondaryId)
                .channelTypeId(params.channelTypeId())
                .channelSubTypeIds(params.channelSubTypeIds())
                .mobileDeviceId(params.mobileDeviceId())
                .mobileOsVersion(params.mobileOsVersion())
                .mobileScreenDensityDpi(params.mobileScreenDensityDpi())
                .videoQuality(params.videoQuality())
                .commentaryLanguage(params.commentaryLanguage())
                .providerId(params.providerId())
                .contentType(params.contentType())
                .streamTypeIds(requestedStreamTypeId != null ? Collections.singleton(requestedStreamTypeId) : TypeStream.REGULAR_STREAM_TYPES)
                .brandIds(Collections.singleton(streamingBrandId))
                .providerParams(params.providerParams())
                .build();

        validateChannelParams(context.uuid(), searchKey);

        return streamService.getStreamInfoByExternalId(searchKey, context, Boolean.TRUE.equals(params.includeMetadata()));
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
