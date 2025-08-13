package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.ScheduleItem;
import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.VideoStreamState;
import org.springframework.stereotype.Service;

@Service
public class AtrScheduleService {

    public VideoStreamState getCachedStreamState(ScheduleItem item, User user) {
        return null;
    }

}
