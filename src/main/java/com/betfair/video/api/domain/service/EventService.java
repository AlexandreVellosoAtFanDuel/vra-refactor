package com.betfair.video.api.domain.service;

import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.entity.VideoScheduleItem;
import com.betfair.video.api.domain.valueobject.ServicePermission;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EventService {

    private final PermissionService permissionService;

    public EventService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public List<VideoScheduleItem> retrieveScheduleByExternalId(User user, String externalIdSource, String externalId,
                                                                Integer channelTypeId, Integer mobileDeviceId) {

        if (!permissionService.userHasPermission(ServicePermission.VIDEO, user)) {
            throw new VideoAPIException(ResponseCode.Forbidden, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS, null);
        }

        return Collections.emptyList();
    }

}
