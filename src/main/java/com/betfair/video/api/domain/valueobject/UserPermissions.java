package com.betfair.video.api.domain.valueobject;

import java.util.Set;

public record UserPermissions(
        Set<String> services,
        Set<Character> mappingStatuses,
        Set<Character> mappingApprovalStatuses,
        Set<Character> importStatuses,
        Set<Character> importApprovalStatuses
) {

    public boolean hasPermission(final ServicePermission servicePermission) {
        return services.contains(servicePermission.name());
    }

}
