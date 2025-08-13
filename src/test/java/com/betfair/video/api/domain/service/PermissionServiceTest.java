package com.betfair.video.api.domain.service;

import com.betfair.video.api.domain.valueobject.ServicePermission;
import com.betfair.video.api.domain.valueobject.UserPermissions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {PermissionService.class})
@DisplayName("PermissionService Tests")
class PermissionServiceTest {

    @Nested
    @DisplayName("AllPermissionLevels Tests")
    @TestPropertySource(properties = {
            "user.permissions.services=ALL",
    })
    class AllPermissionLevelsTest {
        @Autowired
        private PermissionService permissionService;

        @Test
        @DisplayName("Should validate user services for ALL")
        void shouldValidateUserServicesForAll() {
            UserPermissions userPermissions = permissionService.createUserPermissions();

            assertThat(userPermissions.hasPermission(ServicePermission.VIDEO))
                    .isTrue();
            assertThat(userPermissions.hasPermission(ServicePermission.SCHEDULE))
                    .isTrue();
            assertThat(userPermissions.hasPermission(ServicePermission.REFERENCE))
                    .isTrue();
        }
    }

    @Nested
    @DisplayName("SpecificPermissionLevels Tests")
    @TestPropertySource(properties = {
            "user.permissions.services=SCHEDULE,VIDEO",
    })
    class SpecificPermissionLevels {
        @Autowired
        private PermissionService permissionService;

        @Test
        @DisplayName("Should validate user services for specific service")
        void shouldValidateUserServicesForSpecificService() {
            UserPermissions userPermissions = permissionService.createUserPermissions();

            assertThat(userPermissions.hasPermission(ServicePermission.VIDEO))
                    .isTrue();
            assertThat(userPermissions.hasPermission(ServicePermission.SCHEDULE))
                    .isTrue();
            assertThat(userPermissions.hasPermission(ServicePermission.REFERENCE))
                    .isFalse();
        }
    }


}
