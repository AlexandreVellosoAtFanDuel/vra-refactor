package com.betfair.video.output.adapter;

import com.betfair.video.domain.port.output.DirectStreamConfigPort;
import org.springframework.stereotype.Component;

@Component
public class DirectStreamConfigAdapter implements DirectStreamConfigPort {
    @Override
    public boolean isArchivedProviderInList(Integer integer, boolean b) {
        return false;
    }

    @Override
    public boolean isProviderInList(Integer integer, Integer integer1) {
        // TODO: fetch from configuration
        return true;
    }

}
