package com.betfair.video.output.client.impl;

import com.betfair.video.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.domain.exception.InsufficientAccessException;
import com.betfair.video.output.client.CROClient;
import com.betfair.video.output.dto.ResponseVerifySession;
import com.betfair.video.output.dto.cro.RequestVerifySession;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CROClientImpl implements CROClient {

    private final RestClient croClient;

    public CROClientImpl(RestClient croClient) {
        this.croClient = croClient;
    }

    @Override
    public ResponseVerifySession verifySession(RequestVerifySession requestBody) {
        return croClient.post()
                .uri("/SessionManagement/v1/verifySession")
                .body(requestBody)
                .retrieve()
                .onStatus(status -> HttpStatus.UNAUTHORIZED == status, (request, response) -> {
                    throw new InsufficientAccessException();
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ErrorInDependentServiceException("Error when calling CRO service");
                })
                .body(ResponseVerifySession.class);
    }

}
