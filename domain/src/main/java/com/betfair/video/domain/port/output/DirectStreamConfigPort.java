package com.betfair.video.domain.port.output;

public interface DirectStreamConfigPort {
    boolean isArchivedProviderInList(Integer integer, boolean b);

    boolean isProviderInList(Integer integer, Integer integer1);
}
