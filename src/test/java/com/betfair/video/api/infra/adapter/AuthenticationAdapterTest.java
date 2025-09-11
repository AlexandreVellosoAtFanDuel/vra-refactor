package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.exception.ErrorInDependentServiceException;
import com.betfair.video.api.domain.exception.InsufficientAccessException;
import com.betfair.video.api.domain.exception.VideoException;
import com.betfair.video.api.infra.input.rest.dto.cro.RequestVerifySession;
import com.betfair.video.api.infra.output.adapter.AuthenticationAdapter;
import com.betfair.video.api.infra.output.client.CROClient;
import com.betfair.video.api.infra.output.dto.ResponseVerifySession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.betfair.video.api.domain.exception.VideoException.ErrorCodeEnum.ERROR_IN_DEPENDENT_SERVICE;
import static com.betfair.video.api.domain.exception.VideoException.ErrorCodeEnum.INSUFFICIENT_ACCESS;
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
        when(croClient.verifySession(any(RequestVerifySession.class))).thenThrow(new InsufficientAccessException());

        // When & Then
        assertThatThrownBy(() -> authenticationAdapter.verifySession(INVALID_SESSION_TOKEN))
                .isInstanceOf(VideoException.class)
                .satisfies(exception -> {
                    VideoException videoException = (VideoException) exception;
                    assertThat(videoException.getErrorCode()).isEqualTo(INSUFFICIENT_ACCESS);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

    @Test
    @DisplayName("Should throw unauthorized response for error when validating session")
    void shouldThrowUnauthorizedResponseForErrorWhenValidatingSession() {
        // Given
        when(croClient.verifySession(any(RequestVerifySession.class))).thenThrow(new ErrorInDependentServiceException());

        // When & Then
        assertThatThrownBy(() -> authenticationAdapter.verifySession(INVALID_SESSION_TOKEN))
                .isInstanceOf(VideoException.class)
                .satisfies(exception -> {
                    VideoException videoException = (VideoException) exception;
                    assertThat(videoException.getErrorCode()).isEqualTo(ERROR_IN_DEPENDENT_SERVICE);
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
                .isInstanceOf(VideoException.class)
                .satisfies(exception -> {
                    VideoException videoException = (VideoException) exception;
                    assertThat(videoException.getErrorCode()).isEqualTo(ERROR_IN_DEPENDENT_SERVICE);
                    assertThat(videoException.getSportType()).isNull();
                });
    }

}
