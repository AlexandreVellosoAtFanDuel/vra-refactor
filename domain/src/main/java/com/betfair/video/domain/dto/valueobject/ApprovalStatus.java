package com.betfair.video.domain.dto.valueobject;

public enum ApprovalStatus {

    NOT_APPROVED('N'),
    SUGGESTED_APPROVED('S'),
    ACTIVE_APPROVED('A'),
    BAD('B');

    private final Character status;

    ApprovalStatus(Character c) {
        this.status = c;
    }

    public char getStatus() {
        return this.status;
    }

    public static ApprovalStatus fromValue(Character value) {
        if (value == null) {
            return null;
        }

        for (ApprovalStatus approvalStatus : ApprovalStatus.values()) {
            if (approvalStatus.status.equals(value)) {
                return approvalStatus;
            }
        }

        return null;
    }

}
