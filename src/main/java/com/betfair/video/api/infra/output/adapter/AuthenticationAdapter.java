package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.infra.input.rest.dto.cro.RequestVerifySession;
import com.betfair.video.api.infra.output.dto.ResponseVerifySession;
import com.betfair.video.api.infra.input.rest.dto.cro.SessionToken;
import com.betfair.video.api.infra.input.rest.exception.ResponseCode;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIException;
import com.betfair.video.api.infra.input.rest.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.domain.port.output.AuthenticationPort;
import com.betfair.video.api.infra.output.client.CROClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAdapter implements AuthenticationPort {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAdapter.class);

    private final CROClient croClient;

    public AuthenticationAdapter(CROClient croClient) {
        this.croClient = croClient;
    }

    @Override
    public ResponseVerifySession verifySession(String sessionToken) {
        var request = new RequestVerifySession(new SessionToken(sessionToken), null, "TRUE");

        try {
            return this.croClient.verifySession(request);
        } catch (FeignException fe) {
            if (HttpStatus.UNAUTHORIZED.value() == fe.status()) {
                logger.warn("Session verification failed with status: {}", fe.status());
                throw new VideoAPIException(ResponseCode.Unauthorised, VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS, null);
            }

            logger.warn("Error when verifying session: {}", fe.getMessage());
            throw new VideoAPIException(ResponseCode.Unauthorised, VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE, null);
        } catch (Exception e) {
            logger.error("Error verifying session", e);
            throw new VideoAPIException(ResponseCode.InternalError, VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE, null);
        }
    }

}
