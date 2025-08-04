package com.betfair.video.api.infrastructure.in.exception;

public enum ResponseCode {

    InternalError(FaultCode.Server),
    BusinessException(FaultCode.Server),
    Timeout(FaultCode.Server),
    ServiceUnavailable(FaultCode.Server),
    Unauthorised(FaultCode.Client),
    Forbidden(FaultCode.Client),
    NotFound(FaultCode.Client),
    UnsupportedMediaType(FaultCode.Client),
    MediaTypeNotAcceptable(FaultCode.Client),
    BadRequest(FaultCode.Client),
    CantWriteToSocket(FaultCode.Client),
    Ok(null);

    private final FaultCode faultCode;

    ResponseCode(FaultCode faultCode) {
        this.faultCode = faultCode;
    }

    public FaultCode getFaultCode() {
        return this.faultCode;
    }

}
