package service;

import com.betfair.video.api.domain.dto.valueobject.ServicePermission;
import com.betfair.video.api.domain.dto.valueobject.UserPermissions;
import com.betfair.video.api.domain.service.PermissionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("PermissionService Tests")
class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Test
    @DisplayName("Should validate user services for ALL")
    void shouldValidateUserServicesForAll() {
        // Given
        ReflectionTestUtils.setField(permissionService, "userPermissionsServices", "ALL");

        // When
        UserPermissions userPermissions = permissionService.createUserPermissions();

        // Then
        assertThat(userPermissions.hasPermission(ServicePermission.VIDEO))
                .isTrue();
        assertThat(userPermissions.hasPermission(ServicePermission.SCHEDULE))
                .isTrue();
        assertThat(userPermissions.hasPermission(ServicePermission.REFERENCE))
                .isTrue();
    }

    @Test
    @DisplayName("Should validate user services for specific service")
    void shouldValidateUserServicesForSpecificService() {
        // Given
        ReflectionTestUtils.setField(permissionService, "userPermissionsServices", "SCHEDULE,VIDEO");

        // When
        UserPermissions userPermissions = permissionService.createUserPermissions();

        // Then
        assertThat(userPermissions.hasPermission(ServicePermission.VIDEO))
                .isTrue();
        assertThat(userPermissions.hasPermission(ServicePermission.SCHEDULE))
                .isTrue();
        assertThat(userPermissions.hasPermission(ServicePermission.REFERENCE))
                .isFalse();
    }

}
