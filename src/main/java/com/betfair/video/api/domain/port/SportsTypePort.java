package com.betfair.video.api.domain.port;

import com.betfair.video.api.domain.valueobject.ReferenceType;

public interface SportsTypePort {

    ReferenceType findSportTypeByBetfairSportsType(Integer integer);

}
