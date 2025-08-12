package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.application.dto.cro.RequestVerifySession;
import com.betfair.video.api.application.dto.cro.ResponseVerifySession;
import com.betfair.video.api.application.exception.ResponseCode;
import com.betfair.video.api.application.exception.VideoAPIException;
import com.betfair.video.api.application.exception.VideoAPIExceptionErrorCodeEnum;
import com.betfair.video.api.infra.client.CROClient;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationAdapter Tests")
class AuthenticationAdapterTest {

    @Mock
    private CROClient croClient;

    @InjectMocks
    private AuthenticationAdapter authenticationAdapter;

    private static final String VALID_SESSION_TOKEN = "valid-session-token";

    private static final String INVALID_SESSION_TOKEN = "invalid-session-token";

    @Test
    @DisplayName("Should successfully verify valid session token")
    void shouldSuccessfullyVerifyValidSessionToken() {
        // Given
        ResponseVerifySession mockResponse = mock(ResponseVerifySession.class);
        when(croClient.verifySession(any(RequestVerifySession.class))).thenReturn(mockResponse);

        // When & Then
        assertDoesNotThrow(() -> authenticationAdapter.verifySession(VALID_SESSION_TOKEN));
    }

    @Test
    @DisplayName("Should throw unauthorized response for invalid session")
    void shouldHandleExceptionsGracefullyWhenVerifyingSessionToken() {
        // Given
        FeignException unauthorizedException = mock(FeignException.class);
        when(unauthorizedException.status()).thenReturn(HttpStatus.UNAUTHORIZED.value());
        when(croClient.verifySession(any(RequestVerifySession.class))).thenThrow(unauthorizedException);

        // When & Then
        assertThatThrownBy(() -> authenticationAdapter.verifySession(INVALID_SESSION_TOKEN))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.Unauthorised);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.INSUFFICIENT_ACCESS);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should throw unauthorized response for error when validating session")
    void shouldThrowUnauthorizedResponseForErrorWhenValidatingSession() {
        // Given
        FeignException unauthorizedException = mock(FeignException.class);
        when(unauthorizedException.status()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(croClient.verifySession(any(RequestVerifySession.class))).thenThrow(unauthorizedException);

        // When & Then
        assertThatThrownBy(() -> authenticationAdapter.verifySession(INVALID_SESSION_TOKEN))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.Unauthorised);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should throw internal error when general error happen")
    void shouldThrowInternalErrorWhenGeneralErrorHappen() {
        // Given
        RuntimeException runtimeException = mock(RuntimeException.class);
        when(croClient.verifySession(any(RequestVerifySession.class))).thenThrow(runtimeException);

        // When & Then
        assertThatThrownBy(() -> authenticationAdapter.verifySession(INVALID_SESSION_TOKEN))
                .isInstanceOf(VideoAPIException.class)
                .satisfies(exception -> {
                    VideoAPIException videoException = (VideoAPIException) exception;
                    assertThat(videoException.getResponseCode()).isEqualTo(ResponseCode.InternalError);
                    assertThat(videoException.getErrorCode()).isEqualTo(VideoAPIExceptionErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
