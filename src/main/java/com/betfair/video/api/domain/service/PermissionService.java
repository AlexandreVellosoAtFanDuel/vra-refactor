package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.utils.ScheduleItemUtils;
import com.betfair.video.api.domain.utils.StreamExceptionLoggingUtils;
import com.betfair.video.api.domain.valueobject.ExternalIdSource;
import com.betfair.video.api.domain.valueobject.ServicePermission;
import com.betfair.video.api.domain.valueobject.UserPermissions;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import com.betfair.video.api.domain.valueobject.search.VideoStreamInfoByExternalIdSearchKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Value("${user.permissions.services}")
    private String userPermissionsServices;

    private final StreamExceptionLoggingUtils streamExceptionLoggingUtils;

    public PermissionService(StreamExceptionLoggingUtils streamExceptionLoggingUtils) {
        this.streamExceptionLoggingUtils = streamExceptionLoggingUtils;
    }

    public UserPermissions createUserPermissions() {
        return new UserPermissions(
                getUserServices(),
                getUserValidMappingStatuses(),
                getUserValidMappingApprovalStatuses(),
                getUserValidImportStatuses(),
                getUserValidImportApprovalStatuses()
        );
    }

    private Set<String> getUserServices() {
        Set<String> services = new HashSet<>();

        if ("ALL".equalsIgnoreCase(userPermissionsServices)) {
            services.addAll(Arrays.stream(ServicePermission.values()).toList()
                    .stream()
                    .map(Enum::name)
                    .toList());
        } else {
            services.addAll(Arrays.stream(userPermissionsServices.split(",")).toList()
                    .stream()
                    .map(s -> ServicePermission.valueOf(s.trim()).name())
                    .collect(Collectors.toSet()));
        }

        return services;
    }

    private Set<Character> getUserValidMappingStatuses() {
        return Collections.EMPTY_SET;
    }

    private Set<Character> getUserValidMappingApprovalStatuses() {
        return Collections.EMPTY_SET;
    }

    private Set<Character> getUserValidImportStatuses() {
        return Collections.EMPTY_SET;
    }

    private Set<Character> getUserValidImportApprovalStatuses() {
        return Collections.EMPTY_SET;
    }

    public boolean checkUserPermissionsAgainstItem(ScheduleItem item, User user) {
        return true;
    }

    public ScheduleItem filterScheduleItems(RequestContext context, List<ScheduleItem> items, VideoStreamInfoByExternalIdSearchKey searchKey, VideoRequestIdentifier eventIdentifier) {
        String additionalInfo;

        if (items.isEmpty()) {
            additionalInfo = String.format("{No ScheduleItem found by search key: %s}", searchKey);

            VideoAPIException exception = new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, additionalInfo);
            streamExceptionLoggingUtils.logException(logger, eventIdentifier, Level.WARN, context, exception, items, null);
            throw exception;
        }

        checkUserPermissionsAgainstExternalIdProvider(context.user(), searchKey, eventIdentifier);
        checkUserSession(context.user(), items, eventIdentifier);
        additionalInfo = checkUserPermissionsAgainstItemStatus(items, searchKey, context.user());

        if (items.size() > 1) {
            additionalInfo = filterItemsAgainstContentTypesAndProviders(items, searchKey, context.user());
        }

        if (items.isEmpty()) {
            VideoAPIException exception = new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.STREAM_NOT_FOUND, additionalInfo);

            streamExceptionLoggingUtils.logException(logger, Long.valueOf(eventIdentifier.videoId()), Level.ERROR, context, exception, null);
            throw exception;
        }

        if (items.size() > 1) {
            return pickScheduleItem(items, searchKey, eventIdentifier, context);
        }

        return items.getFirst();
    }

    private String filterItemsAgainstContentTypesAndProviders(List<ScheduleItem> items, VideoStreamInfoByExternalIdSearchKey searchKey, User user) {
        // TODO: Implement method
        return null;
    }

    public void checkUserPermissionsAgainstExternalIdProvider(User user, VideoStreamInfoByExternalIdSearchKey searchKey, VideoRequestIdentifier identifier) {
        // TODO: implement this method
    }

    private void checkUserSession(User user, List<ScheduleItem> items, VideoRequestIdentifier eventIdentifier) {
        // TODO: Implement this method
    }

    private String checkUserPermissionsAgainstItemStatus(List<ScheduleItem> items, VideoStreamInfoByExternalIdSearchKey searchKey, User user) {
        StringBuilder additionalInfo = new StringBuilder(String.format("Method: checkUserPermissionsAgainstItemStatus, SearchKey: %s, AccountId: %s, "
                        + "ScheduleItems to be filtered list size: %s ", searchKey, user.accountId(),
                items.size()));

        if (user.permissions() == null) {
            return additionalInfo.toString();
        }

        for (Iterator<ScheduleItem> iterator = items.iterator(); iterator.hasNext(); ) {
            ScheduleItem item = iterator.next();
            boolean itemIsAvailable = itemIsAvailable(item, user);
            boolean userHasMappingStatusPermissions = hasUserMappingStatusPermissions(searchKey, item, user);
            boolean hasUserImportStatusPermissions = hasUserImportStatusPermissions(item, user);

            if (!itemIsAvailable) {
                additionalInfo.append(String.format("{ScheduleItem with id '%s' is not available. Import status - '%s'}",
                        item.videoItemId(), item.importStatus()));
            } else {
                if (!userHasMappingStatusPermissions) {
                    additionalInfo.append(String.format("{User don't have mapping status permissions. MappingStatusPermissions: %s, "
                                    + "MappingApprovalStatusPermissions: %s, VideoId: %s, Mappings: %s}",
                            user.permissions().mappingStatuses(), user.permissions().mappingApprovalStatuses(),
                            item.videoItemId(), item.mappings()));
                }

                if (!hasUserImportStatusPermissions) {
                    additionalInfo.append(String.format("{User don't have import permissions. ImportStatusPermissions: %s, "
                                    + "ImportApprovalStatusPermissions: %s, VideoId: %s, ImportStatus: %s, ImportApprovalStatus: %s}",
                            user.permissions().importStatuses(), user.permissions().importApprovalStatuses(),
                            item.videoItemId(), item.importStatus(), item.approvalStatus()));
                }
            }

            if (!userHasMappingStatusPermissions || !hasUserImportStatusPermissions || !itemIsAvailable) {
                iterator.remove();
            }
        }

        return additionalInfo.toString();
    }

    private boolean itemIsAvailable(ScheduleItem item, User user) {
        // TODO: Implement method
        return true;
    }

    private boolean hasUserMappingStatusPermissions(VideoStreamInfoByExternalIdSearchKey searchKey, ScheduleItem item, User user) {
        // TODO: Implement method
        return true;
    }

    private boolean hasUserImportStatusPermissions(ScheduleItem item, User user) {
        // TODO: Implement method
        return true;
    }

    private ScheduleItem pickScheduleItem(List<ScheduleItem> items, VideoStreamInfoByExternalIdSearchKey searchKey, VideoRequestIdentifier eventIdentifier, RequestContext context) {
        ScheduleItem pickedScheduleItem = null;

        if (isRacingSport(items.getFirst().betfairSportsType())) {
            //if racing stream was requested by event id - just throw error since we cannot know which stream to return between several ones
            if (eventIdentifier.marketId() == null && eventIdentifier.exchangeRaceId() == null && eventIdentifier.rampId() == null) {
                logger.error("[{}]: Got racing stream request by event id {}! Cannot uniquely resolve stream.",
                        context.uuid(), eventIdentifier.eventId());

                VideoAPIException exception = new VideoAPIException(ResponseCode.NotFound, VideoAPIExceptionErrorCodeEnum.CANNOT_UNIQUELY_RESOLVE_STREAM, null);
                streamExceptionLoggingUtils.logException(logger, Long.valueOf(eventIdentifier.videoId()), Level.ERROR, context, exception, null);
                throw exception;
            } else {
                pickedScheduleItem = getItemWithHighestProviderEventId(items);
            }
        } else {
            // A search by external IDs resulted in multiple video streams.
            // Returning stream with Exact mapping or the one with the highest inexact total score if requested by eventId.
            // Otherwise - returning one with the highest provider event id and logging issue
            if (searchKey.getExternalIdSource() == ExternalIdSource.BETFAIR_EVENT) {
                logger.info("[{}]: Cannot uniquely resolve stream! Returning item with Exact mapping or highest inexact score. Search key: {}. Resolved items: {}",
                        context.uuid(), searchKey, ScheduleItemUtils.getItemsForLog(items));
                pickedScheduleItem = ScheduleItemUtils.pickMaxScoredStream(searchKey.getPrimaryId(), items);
            } else {
                logger.info("[{}]: Cannot uniquely resolve stream! Returning item with higher provider event id. Video id: {}. Search key: {}. Resolved items: {}",
                        context.uuid(), items.getFirst().videoItemId(), searchKey, ScheduleItemUtils.getItemsForLog(items));

                pickedScheduleItem = getItemWithHighestProviderEventId(items);
            }
            logger.info("[{}]: Selected stream: VideoId={}, ImportSt={}, ApprovalSt={}",
                    context.uuid(),
                    pickedScheduleItem.videoItemId(),
                    pickedScheduleItem.importStatus(),
                    pickedScheduleItem.approvalStatus());
        }
        return pickedScheduleItem;
    }

    private ScheduleItem getItemWithHighestProviderEventId(List<ScheduleItem> items) {
        // TODO: Implement method
        return null;
    }

    private boolean isRacingSport(Integer integer) {
        // TODO: Implement method
        return false;
    }

}
