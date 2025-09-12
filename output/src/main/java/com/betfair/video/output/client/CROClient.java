package com.betfair.video.output.client;

import com.betfair.video.output.dto.ResponseVerifySession;
import com.betfair.video.output.dto.cro.RequestVerifySession;

public interface CROClient {

    ResponseVerifySession verifySession(RequestVerifySession requestBody);

}
