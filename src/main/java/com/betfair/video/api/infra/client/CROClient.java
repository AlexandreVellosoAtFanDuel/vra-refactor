package com.betfair.video.api.infra.client;

import com.betfair.video.api.application.dto.cro.RequestVerifySession;
import com.betfair.video.api.application.dto.cro.ResponseVerifySession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cro-client", url = "${cro.api.base-url}")
public interface CROClient {

    @PostMapping("/SessionManagement/v1/verifySession")
    ResponseVerifySession verifySession(@RequestBody RequestVerifySession request);

}
