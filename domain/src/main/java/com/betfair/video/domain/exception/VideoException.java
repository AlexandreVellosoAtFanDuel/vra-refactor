package com.betfair.video.domain.exception;

public class VideoException extends RuntimeException {

    private static final ErrorCodeEnum DEFAULT_ERROR_CODE = ErrorCodeEnum.GENERIC_ERROR;
    private static final String DEFAULT_MESSAGE = "Generic error";

    protected final String sportType;

    protected final ErrorCodeEnum errorCode;

    public VideoException() {
        super(DEFAULT_MESSAGE);
        this.errorCode = DEFAULT_ERROR_CODE;
        this.sportType = null;
    }

    public VideoException(ErrorCodeEnum errorCode, String sportType) {
        super(DEFAULT_MESSAGE);
        this.errorCode = errorCode;
        this.sportType = sportType;
    }

    public VideoException(ErrorCodeEnum errorCode, String message, String sportType) {
        super(message);
        this.errorCode = errorCode;
        this.sportType = sportType;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getSportType() {
        return sportType;
    }

    // TODO: Verify a better place for this enum
    public enum ErrorCodeEnum {

        /**
         * HTTP 400 Bad Request. The consumer has not provided conditional data that is required based on other values
         */
        CONDITIONAL_DATA_MISSING(1),

        /**
         * HTTP 400 Bad Request. The consumer has specified invalid input information
         */
        INVALID_INPUT(2),

        /**
         * HTTP 400 Bad Request. The consumer is deemed to not have a valid session
         */
        NO_SESSION(3),

        /**
         * HTTP 400 Bad Request. The consuming application is not known to the service
         */
        UNKNOWN_CONSUMER(4),

        /**
         * HTTP 403 Forbidden. While the consumer has certain access permissions they are insufficient for the requested operation or data
         */
        INSUFFICIENT_ACCESS(10),

        /**
         * HTTP 403 Forbidden. The consumer is trying to access data from a restricted country
         */
        RESTRICTED_COUNTRY(11),

        /**
         * HTTP 403 Forbidden. The consumer holds insufficient funds in their wallets to be permitted to watch video
         */
        INSUFFICIENT_FUNDING(12),

        /**
         * HTTP 403 Forbidden. The consumer is deemed to have not placed sufficient stakes on qualifying bets to watch video
         */
        BBV_INSUFFICIENT_STAKES(13),

        /**
         * HTTP 403 Forbidden. The consumer is deemed not to have placed any qualifying bets to watch video
         */
        BBV_NO_STAKES(14),

        /**
         * HTTP 500 Internal Server Error. The streaming partner has returned an unknown or generic error; or a particular error
         */
        STREAM_GENERIC_ERROR(30),

        /**
         * HTTP 404 Not Found. The consumer has attempted to access a stream that the adapter.provider cannot find. This could be because the stream no longer exists or an invalid id has been provided
         */
        STREAM_NOT_FOUND(31),

        /**
         * HTTP 404 OK. The consumer has attempted to access a stream that is not yet available
         */
        STREAM_NOT_STARTED(32),

        /**
         * HTTP 404 Not Found. The consumer has attempted to access a stream that is no longer on air
         */
        STREAM_HAS_ENDED(33),

        /**
         * HTTP 403 Forbidden. The streaming adapter.provider is denying the customer access to the video event. Reasons vary from adapter.provider to adapter.provider
         */
        STREAM_ACCESS_DENIED(34),

        /**
         * HTTP 403 Forbidden. Usage limits breached. The usage limit is a restriction set on the number of views per user per hour for an individual event.
         */
        USAGE_LIMITS_BREACHED(35),

        /**
         * HTTP 500 Internal Server Error. One or more of the downstream services that are used have returned an error
         */
        ERROR_IN_DEPENDENT_SERVICE(50),

        /**
         * HTTP 404 Not Found. The inputs provided by the consumer cannot uniquely resolve to a single video stream e.g. Betfair HR events for UK or Irish racing cover more than one race whereas the video providers have one video stream per race
         */
        CANNOT_UNIQUELY_RESOLVE_STREAM(51),

        /**
         * HTTP 500 Internal Server Error. The streaming adapter.provider has returned an error.
         */
        PROVIDER_CONNECTION_ERROR(52),

        /**
         * HTTP 404 Not Found. The consumer has attempted to access a stream that is not yet available in archive
         */
        NOT_IN_ARCHIVE_YET(53),

        /**
         * HTTP 403 Forbidden. The consumer has attempted to access a stream of adapter.provider that is not available on site (app) request comes from
         */
        PROVIDER_NOT_AVAILABLE_ON_SITE(54),

        /**
         * HTTP 403 Forbidden. The consumer has attempted to access a stream of adapter.provider's venue that is not available on site (app) request comes from
         */
        VENUE_NOT_AVAILABLE_ON_SITE(55),

        /**
         * HTTP 500 Internal Server Error. A generic error has been encountered while processing the request.
         */
        GENERIC_ERROR(100),

        /**
         * HTTP 503 Service unavailable. API Node data state is cold. Unable to supply results.
         */
        COLD_STATE(101),

        UNRECOGNIZED_VALUE(null);

        private final String value;

        ErrorCodeEnum(Integer id) {
            this.value = String.format("VIDX-%04d", id);
        }

        public String getValue() {
            return value;
        }

    }

}