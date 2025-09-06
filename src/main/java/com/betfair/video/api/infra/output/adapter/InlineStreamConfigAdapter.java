package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.port.output.InlineStreamConfigPort;
import org.springframework.stereotype.Component;

@Component
public class InlineStreamConfigAdapter implements InlineStreamConfigPort {

    @Override
    public boolean isArchivedProviderInList(Integer integer, boolean b) {
        return false;
    }

    @Override
    public boolean isProviderInList(Integer integer, Integer integer1) {
        return false;
    }

}
