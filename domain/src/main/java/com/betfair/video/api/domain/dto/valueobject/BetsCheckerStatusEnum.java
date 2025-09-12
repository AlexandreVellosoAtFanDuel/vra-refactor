package com.betfair.video.api.domain.dto.valueobject;

public enum BetsCheckerStatusEnum {

    BBV_FAILED_TECHNICAL_ERROR("bbvTestFailedApiError"),
    BBV_FAILED_INSUFFICIENT_STAKES("bbvTestFailedInsufficientBets"),
    BBV_FAILED_NO_BETS("bbvTestFailedNoBets"),
    BBV_PASSED("bbvTestPassed"),
    BBV_NOT_REQUIRED_CONFIG("bbvTestNotRequiredConfigPassed"),
    BBV_NOT_REQUIRED_SUPERUSER("bbvTestNotRequiredSuperUserPassed"),
    BBV_FAILED_INSUFFICIENT_FUNDS("bbvTestFailedInsufficientFunds"),
    BBV_FAILED_DEPENDENT_SERVICE_ERROR("bbvTestFailedDependentServiceError");


    private String status;

    BetsCheckerStatusEnum(String status) {
        this.status = status;
    }
}
