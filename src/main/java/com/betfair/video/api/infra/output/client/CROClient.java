package com.betfair.video.api.infra.output.client;

import com.betfair.video.api.infra.input.rest.dto.cro.RequestVerifySession;
import com.betfair.video.api.infra.output.dto.ResponseVerifySession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cro-client", url = "${cro.api.base-url}")
public interface CROClient {

    @PostMapping("/SessionManagement/v1/verifySession")
    ResponseVerifySession verifySession(@RequestBody RequestVerifySession request);

}
