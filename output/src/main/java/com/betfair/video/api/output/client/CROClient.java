package com.betfair.video.api.output.client;

import com.betfair.video.api.output.dto.ResponseVerifySession;
import com.betfair.video.api.output.dto.cro.RequestVerifySession;

public interface CROClient {

    ResponseVerifySession verifySession(RequestVerifySession requestBody);

}
