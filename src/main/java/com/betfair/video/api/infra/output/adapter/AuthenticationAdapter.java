package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.dto.valueobject.UserSessionDto;
import com.betfair.video.api.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.api.domain.exception.InsufficientAccessException;
import com.betfair.video.api.domain.port.output.AuthenticationPort;
import com.betfair.video.api.infra.input.rest.dto.cro.RequestVerifySession;
import com.betfair.video.api.infra.input.rest.dto.cro.SessionToken;
import com.betfair.video.api.infra.output.client.CROClient;
import com.betfair.video.api.infra.output.dto.ResponseVerifySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        } catch (InsufficientAccessException ex) {
            logger.info("Session verification failed", ex);
            throw ex;
        }catch (ErrorInDependentServiceException ex) {
            logger.info("Error in CRO service during session verification", ex);
            throw ex;
        } catch (Exception e) {
            logger.error("General error verifying session", e);
            throw new ErrorInDependentServiceException();
        }
    }

}
