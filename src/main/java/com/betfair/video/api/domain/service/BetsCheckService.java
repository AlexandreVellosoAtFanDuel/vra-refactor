package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.BetsCheckerStatusEnum;
import com.betfair.video.api.domain.valueobject.search.VideoRequestIdentifier;
import org.springframework.stereotype.Service;

@Service
public class BetsCheckService {

    public BetsCheckerStatusEnum getBBVStatus(VideoRequestIdentifier identifier, ScheduleItem item, User user, boolean isArchivedStream) {
        return BetsCheckerStatusEnum.BBV_NOT_REQUIRED_CONFIG;
    }

}
