package com.betfair.video.api.infra.adapter.provider;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.Provider;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.port.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.StreamingProviderPort;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.domain.valueobject.StreamDetails;
import com.betfair.video.api.domain.valueobject.StreamParams;
import com.betfair.video.api.domain.valueobject.StreamingFormat;
import com.betfair.video.api.domain.valueobject.VideoQuality;
import com.betfair.video.api.infra.client.BetRadarV2Client;
import com.betfair.video.api.infra.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.api.infra.dto.betradarv2.ContentDto;
import com.betfair.video.api.infra.dto.betradarv2.StreamDto;
import com.betfair.video.api.infra.dto.betradarv2.StreamUrlDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class BetradarV2Adapter implements StreamingProviderPort {

    private static final Logger logger = LoggerFactory.getLogger(BetradarV2Adapter.class);

    private static final int CACHE_COMPENSATION_TIME_MILLS = 5 * 1000;

    @Value("${provider.betradar.v2.enabled}")
    private String isEnabled;

    @Value("${provider.betradar.v2.recommended.stream.product.ids}")
    private String recommendedStreamStatusIds;

    @Value("${provider.betradar.v2.recommended.stream.status.ids}")
    private String recommendedStreamProductIds;

    private final BetRadarV2Client betRadarV2Client;

    private final ConfigurationItemsPort configurationItemsRepository;

    private final StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    public BetradarV2Adapter(BetRadarV2Client betRadarV2Client, ConfigurationItemsPort configurationItemsRepository, StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        this.betRadarV2Client = betRadarV2Client;
        this.configurationItemsRepository = configurationItemsRepository;
        this.streamExceptionLoggingUtils = streamExceptionLoggingUtils;
    }

    private enum RequestParams {
        STREAM_PROVIDER_ID, CHANNEL_ID, USER_IP, STREAM_FORMAT
    }

    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(isEnabled);
    }

    @Override
    public StreamDetails getStreamDetails(ScheduleItem item, RequestContext context, StreamParams streamParams) {
        AudioVisualEventDto event = getAudioVisualEventByScheduleItem(item)
                .orElseThrow(() -> {
                            VideoAPIException exception = createStreamNotActiveException(item);
                            streamExceptionLoggingUtils.logException(logger, item.videoItemId(), Level.ERROR, context, exception, null);
                            return exception;
                        }
                );

        Optional<StreamDto> availableStream = findFirstStreamByStatusOrProductName(event, getRecommendedStreamStatusIds(), getRecommendedStreamProductIds());
        if (availableStream.isEmpty()) {
            final String eventId = parseIdFromScheme(event.id());

            logger.error("[{}]: Cannot find suitable stream for event {}. User: {}",
                    context.uuid(), eventId, context.user().accountId());

            throw new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, String.valueOf(item.betfairSportsType()));
        }

        StreamingFormat streamFormat = configurationItemsRepository.findPreferredStreamingFormat(Provider.BETRADAR_V2,
                item.videoChannelType(), item.betfairSportsType(), item.streamTypeId(), item.brandId());
        if (streamFormat == null) {
            logger.info("There is no default stream format for provider {}, channel {}, stream type {}. Using 'rtmp' as default stream format.", Provider.BETRADAR_V2, item.videoChannelType(), item.streamTypeId());
            streamFormat = StreamingFormat.RTMP;
        }

        Map<String, String> requestParams = buildRequestParams(context.user(), item, streamFormat);

        String providerStreamFormatName = switch (streamFormat) {
            case CMAF_DASH -> "cmaf-manifest";
            case MPEG_DASH -> "dash-manifest";
            case HLS -> "hls-manifest";
            default -> streamFormat.getValue();
        };

        StreamUrlDto streamUrl = getStreamLink(availableStream.get().id(), providerStreamFormatName, requestParams.get(RequestParams.USER_IP.name()));

        return convertToStreamDetails(streamUrl, requestParams);
    }

    @Override
    public Set<VideoQuality> getAvailableVideoQualityValues() {
        // TODO: implement actual logic to fetch available video quality values
        return Set.of(VideoQuality.HIGH);
    }

    private Optional<AudioVisualEventDto> getAudioVisualEventByScheduleItem(ScheduleItem scheduleItem) {
        List<AudioVisualEventDto> events = getAudioVisualEvents();

        return events.stream()
                .filter(eventDto -> parseIdFromScheme(eventDto.id()).equals(scheduleItem.providerEventId()))
                .findFirst();
    }

    private List<AudioVisualEventDto> getAudioVisualEvents() {
        // TODO: Implement cache here
        return betRadarV2Client.getAudioVisualEvents(recommendedStreamStatusIds, recommendedStreamProductIds);
    }

    private StreamUrlDto getStreamLink(String streamId, String streamType, String userIP) {
        return betRadarV2Client.getStreamLink(streamId, streamType, userIP);
    }

    private StreamDetails convertToStreamDetails(StreamUrlDto response, Map<String, String> requestParams) {
        StreamingFormat streamFormat = StreamingFormat.fromValue(requestParams.get(RequestParams.STREAM_FORMAT.name()));

        Map<String, String> params = new HashMap<>();

        params.put("streamFormat", streamFormat.getValue());

        if (StreamingFormat.RTMP.equals(streamFormat)) {
            params.put("streamName", response.streamName());
        }

        return new StreamDetails(response.url(), VideoQuality.HIGH, params);
    }

    private VideoAPIException createStreamNotActiveException(ScheduleItem scheduleItem) {
        VideoAPIExceptionErrorCodeEnum errorCode = VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND;
        if (scheduleItem.providerData() != null && scheduleItem.providerData().getStart() != null) {
            Date eventStartDateCompensated = new Date(scheduleItem.providerData().getStart().getTime() + CACHE_COMPENSATION_TIME_MILLS);

            if (eventStartDateCompensated.after(DateUtils.getCurrentDate())) {
                //event hasn't started yet OR has just started and might be missing in on-air-streams-cache
                errorCode = VideoAPIExceptionErrorCodeEnum.STREAM_NOT_STARTED;
            } else {
                errorCode = VideoAPIExceptionErrorCodeEnum.STREAM_HAS_ENDED;
            }
        }

        return new VideoAPIException(ResponseCode.NotFound, errorCode, String.valueOf(scheduleItem.betfairSportsType()));
    }

    private Optional<StreamDto> findFirstStreamByStatusOrProductName(AudioVisualEventDto event, List<String> streamStatusIds, List<String> streamProductIds) {
        return event.contents()
                .stream()
                .filter(ContentDto::isMain)
                .flatMap(content -> content.streams().stream())
                .filter(stream -> streamHasValidStreamStatusIds(stream, streamStatusIds) && streamHasValidProductIds(stream, streamProductIds))
                .findFirst();
    }

    private boolean streamHasValidStreamStatusIds(StreamDto stream, List<String> streamStatusIds) {
        return CollectionUtils.isEmpty(streamStatusIds) || streamStatusIds.contains(parseIdFromScheme(stream.streamStatus().id()));
    }

    private boolean streamHasValidProductIds(StreamDto stream, List<String> streamProductIds) {
        return CollectionUtils.isEmpty(streamProductIds) || streamProductIds.contains(parseIdFromScheme(stream.product().id()));
    }

    private List<String> getRecommendedStreamStatusIds() {
        return Arrays.stream(recommendedStreamStatusIds.split(","))
                .map(String::trim)
                .toList();
    }

    private List<String> getRecommendedStreamProductIds() {
        return Arrays.stream(recommendedStreamProductIds.split(","))
                .map(String::trim)
                .toList();
    }

    private static String parseIdFromScheme(String idScheme) {
        if (StringUtils.isBlank(idScheme)) {
            return "";
        }

        return Arrays.stream(idScheme.split(":"))
                .reduce((first, second) -> second)
                .orElse(idScheme);
    }

    private Map<String, String> buildRequestParams(User user, ScheduleItem scheduleItem, StreamingFormat streamingFormat) {
        Map<String, String> requestParams = new HashMap<>();

        if (user != null) {
            requestParams.put(RequestParams.USER_IP.name(), user.ip());
        }

        requestParams.put(RequestParams.STREAM_PROVIDER_ID.name(), scheduleItem.providerEventId());
        requestParams.put(RequestParams.CHANNEL_ID.name(), String.valueOf(scheduleItem.videoChannelType()));
        requestParams.put(RequestParams.STREAM_FORMAT.name(), streamingFormat.getValue());

        return requestParams;
    }

}
