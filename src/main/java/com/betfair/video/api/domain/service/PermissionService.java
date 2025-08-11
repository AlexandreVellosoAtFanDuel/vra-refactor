package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.entity.User;
import com.betfair.video.api.domain.valueobject.ServicePermission;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean userHasPermission(final ServicePermission servicePermission, final User user) {
        return true;
    }

}
