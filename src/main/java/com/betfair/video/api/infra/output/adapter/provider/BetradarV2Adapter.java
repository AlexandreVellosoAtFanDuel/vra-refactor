package com.betfair.video.api.infra.output.adapter.provider;

import com.betfair.video.api.domain.dto.entity.Provider;
import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.entity.ScheduleItem;
import com.betfair.video.api.domain.dto.entity.User;
import com.betfair.video.api.domain.dto.valueobject.StreamDetails;
import com.betfair.video.api.domain.dto.valueobject.StreamParams;
import com.betfair.video.api.domain.dto.valueobject.StreamingFormat;
import com.betfair.video.api.domain.dto.valueobject.VideoQuality;
import com.betfair.video.api.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.api.domain.exception.StreamHasEndedException;
import com.betfair.video.api.domain.exception.StreamNotFoundException;
import com.betfair.video.api.domain.exception.StreamNotStartedException;
import com.betfair.video.api.domain.exception.VideoException;
import com.betfair.video.api.domain.port.output.ConfigurationItemsPort;
import com.betfair.video.api.domain.port.output.StreamingProviderPort;
import com.betfair.video.api.domain.utils.DateUtils;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.infra.output.client.BetRadarV2Client;
import com.betfair.video.api.infra.output.dto.betradarv2.AudioVisualEventDto;
import com.betfair.video.api.infra.output.dto.betradarv2.ContentDto;
import com.betfair.video.api.infra.output.dto.betradarv2.StreamDto;
import com.betfair.video.api.infra.output.dto.betradarv2.StreamUrlDto;
import com.hazelcast.map.IMap;
import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Qualifier(value = "betRadarV2AudioVisualEventsMap")
    private final IMap<String, List<AudioVisualEventDto>> audioVisualEvents;

    private final BetRadarV2Client betRadarV2Client;

    private final ConfigurationItemsPort configurationItemsRepository;

    private final StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    public BetradarV2Adapter(IMap<String, List<AudioVisualEventDto>> audioVisualEvents, BetRadarV2Client betRadarV2Client, ConfigurationItemsPort configurationItemsRepository, StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        this.audioVisualEvents = audioVisualEvents;
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
    public StreamDetails getStreamDetails(RequestContext context, ScheduleItem item, StreamParams streamParams) {
        AudioVisualEventDto event = getAudioVisualEventByScheduleItem(context, item);

        StreamDto availableStream = findFirstStreamByStatusOrProductName(context, event, getRecommendedStreamStatusIds(), getRecommendedStreamProductIds(), item.betfairSportsType());

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

        StreamUrlDto streamUrl = getStreamLink(context, availableStream.id(), providerStreamFormatName, requestParams.get(RequestParams.USER_IP.name()));

        return convertToStreamDetails(streamUrl, requestParams);
    }

    @Override
    public Set<VideoQuality> getAvailableVideoQualityValues() {
        return Set.of(VideoQuality.HIGH);
    }

    private AudioVisualEventDto getAudioVisualEventByScheduleItem(RequestContext context, ScheduleItem scheduleItem) {
        List<AudioVisualEventDto> events = fetchAudioVisualEvents(context);

        return events.stream()
                .filter(eventDto -> parseIdFromScheme(eventDto.id()).equals(scheduleItem.providerEventId()))
                .findFirst()
                .orElseThrow(() -> {
                            VideoException exception = createStreamNotActiveException(scheduleItem);
                            streamExceptionLoggingUtils.logException(logger, scheduleItem.videoItemId(), Level.ERROR, context, exception, null);
                            return exception;
                        }
                );
    }

    private List<AudioVisualEventDto> fetchAudioVisualEvents(RequestContext context) {
        List<AudioVisualEventDto> cachedEvents = this.audioVisualEvents.get("audioVisualEvents");

        if (cachedEvents == null || cachedEvents.isEmpty()) {
            logger.info("[{}] Betradar V2 AudioVisualEvents cache miss - fetch events from Betradar V2 service", context.uuid());

            try {
                cachedEvents = betRadarV2Client.getAudioVisualEvents(recommendedStreamStatusIds, recommendedStreamProductIds);
                this.audioVisualEvents.put("audioVisualEvents", cachedEvents);
            } catch (FeignException fe) {
                logger.error("[{}]: Feign error trying to fetch audio visual events from provider for httpStatus = {}, recommendedStreamStatusIds {} and recommendedStreamProductIds {}. Exception: {}",
                        context.uuid(), fe.status(), recommendedStreamStatusIds, recommendedStreamProductIds, fe.getMessage(), fe);
                throw new ErrorInDependentServiceException("General error trying to fetch audio visual events from provider", null);
            } catch (Exception ex) {
                logger.error("[{}] General error trying to fetch audio visual events from provider for recommendedStreamStatusIds {} and recommendedStreamProductIds {}. Exception: {}",
                        context.uuid(), recommendedStreamStatusIds, recommendedStreamProductIds, ex.getMessage(), ex);
                throw new ErrorInDependentServiceException("General error trying to fetch audio visual events from provider", null);
            }
        }

        return cachedEvents;
    }

    private StreamUrlDto getStreamLink(RequestContext context, String streamId, String streamType, String userIP) {
        try {
            return betRadarV2Client.getStreamLink(streamId, streamType, userIP);
        } catch (FeignException fe) {
            if (HttpStatus.NOT_FOUND.value() == fe.status()) {
                throw new StreamNotFoundException("Straem link not found", null);
            }

            logger.error("[{}]: Feign error trying to fetch stream link for httpStatus = {}, streamId {} and streamType {}. Exception: {}", context.uuid(), fe.status(), streamId, streamType, fe.getMessage(), fe);
        } catch (Exception ex) {
            logger.error("[{}] General error trying to fetch stream link for streamId {} and streamType {}. Exception: {}", context.uuid(), streamId, streamType, ex.getMessage(), ex);
        }

        throw new ErrorInDependentServiceException("General error trying to fetch stream link", null);
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

    private VideoException createStreamNotActiveException(ScheduleItem scheduleItem) {
        VideoException.ErrorCodeEnum errorCode = VideoException.ErrorCodeEnum.STREAM_NOT_FOUND;

        if (scheduleItem.providerData() != null && scheduleItem.providerData().getStart() != null) {
            Date eventStartDateCompensated = new Date(scheduleItem.providerData().getStart().getTime() + CACHE_COMPENSATION_TIME_MILLS);

            if (eventStartDateCompensated.after(DateUtils.getCurrentDate())) {
                //event hasn't started yet OR has just started and might be missing in on-air-streams-cache
                errorCode = VideoException.ErrorCodeEnum.STREAM_NOT_STARTED;
            } else {
                errorCode = VideoException.ErrorCodeEnum.STREAM_HAS_ENDED;
            }
        }

        return switch (errorCode) {
            case STREAM_NOT_STARTED -> new StreamNotStartedException(String.valueOf(scheduleItem.betfairSportsType()));
            case STREAM_HAS_ENDED -> new StreamHasEndedException(String.valueOf(scheduleItem.betfairSportsType()));
            default -> new StreamNotFoundException(String.valueOf(scheduleItem.betfairSportsType()));
        };
    }

    private StreamDto findFirstStreamByStatusOrProductName(RequestContext context, AudioVisualEventDto event, List<String> streamStatusIds, List<String> streamProductIds, Integer betfairSportsType) {
        return event.contents()
                .stream()
                .filter(ContentDto::isMain)
                .flatMap(content -> content.streams().stream())
                .filter(stream -> streamHasValidStreamStatusIds(stream, streamStatusIds) && streamHasValidProductIds(stream, streamProductIds))
                .findFirst()
                .orElseThrow(() -> {
                    final String eventId = parseIdFromScheme(event.id());

                    logger.error("[{}]: Cannot find suitable stream for event {}. User: {}",
                            context.uuid(), eventId, context.user().accountId());

                    return new StreamNotFoundException(String.valueOf(betfairSportsType));
                });
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
