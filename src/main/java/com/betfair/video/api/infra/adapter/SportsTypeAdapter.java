package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.port.SportsTypePort;
import com.betfair.video.api.domain.valueobject.ReferenceType;
import org.springframework.stereotype.Component;

@Component
public class SportsTypeAdapter implements SportsTypePort {

    @Override
    public ReferenceType findSportTypeByBetfairSportsType(Integer integer) {
        // TODO: Implement actual caching and fetching logic here
        return new ReferenceType("-1", "Other sports types");
    }
}
