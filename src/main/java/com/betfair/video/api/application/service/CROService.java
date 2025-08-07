package com.betfair.video.api.application.service;

import com.betfair.video.api.infra.CROClient;
import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.application.dto.cro.RequestVerifySession;
import com.betfair.video.api.application.dto.cro.ResponseVerifySession;
import com.betfair.video.api.application.dto.cro.SessionToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import feign.FeignException;

@Service
public class CROService {

    private static final Logger logger = LoggerFactory.getLogger(CROService.class);

    private final CROClient croClient;

    public CROService(CROClient croClient) {
        this.croClient = croClient;
    }

    public void verifySession(String sessionToken) {
        try {
            var request = new RequestVerifySession(new SessionToken(sessionToken), null, "TRUE");

            croClient.verifySession(request);
        } catch (FeignException fe) {
            logger.warn("Error verifying CRO session: {}", fe.getMessage());
            throw new VideoAPIException(ResponseCode.Unauthorised, VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE, null);
        } catch (Exception e) {
            logger.error("Error verifying CRO session", e);
            throw new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE, null);
        }
    }

}
