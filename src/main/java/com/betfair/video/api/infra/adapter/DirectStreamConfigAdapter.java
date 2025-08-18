package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.port.DirectStreamConfigPort;
import org.springframework.stereotype.Component;

@Component
public class DirectStreamConfigAdapter implements DirectStreamConfigPort {
    @Override
    public boolean isArchivedProviderInList(Integer integer, boolean b) {
        return false;
    }

    @Override
    public boolean isProviderInList(Integer integer, Integer integer1) {
        return false;
    }

}
