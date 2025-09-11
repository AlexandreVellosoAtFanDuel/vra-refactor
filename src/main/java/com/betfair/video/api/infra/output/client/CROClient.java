package com.betfair.video.api.infra.output.client;

import com.betfair.video.api.infra.input.rest.dto.cro.RequestVerifySession;
import com.betfair.video.api.infra.output.dto.ResponseVerifySession;

public interface CROClient {

    ResponseVerifySession verifySession(RequestVerifySession requestBody);

}
