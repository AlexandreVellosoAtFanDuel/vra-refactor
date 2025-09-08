package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.dto.valueobject.UserSessionDto;
import com.betfair.video.api.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.api.domain.exception.InsufficientAccessException;
import com.betfair.video.api.domain.port.output.AuthenticationPort;
import com.betfair.video.api.infra.input.rest.dto.cro.RequestVerifySession;
import com.betfair.video.api.infra.input.rest.dto.cro.SessionToken;
import com.betfair.video.api.infra.output.client.CROClient;
import com.betfair.video.api.infra.output.dto.ResponseVerifySession;
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
    public UserSessionDto verifySession(String sessionToken) {
        var request = new RequestVerifySession(new SessionToken(sessionToken), null, "TRUE");

        try {
            ResponseVerifySession session = this.croClient.verifySession(request);
            return new UserSessionDto(session.accountId(), session.userId());
        } catch (FeignException fe) {
            if (HttpStatus.UNAUTHORIZED.value() == fe.status()) {
                logger.warn("Session verification failed with status: {}", fe.status());
                throw new InsufficientAccessException();
            }

            logger.warn("Error when verifying session: {}", fe.getMessage());
            throw new ErrorInDependentServiceException();
        } catch (Exception e) {
            logger.error("Error verifying session", e);
            throw new ErrorInDependentServiceException();
        }
    }

}
