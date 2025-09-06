package com.betfair.video.api.infra.adapter;

import com.betfair.video.api.domain.dto.entity.RequestContext;
import com.betfair.video.api.domain.dto.valueobject.Geolocation;
import com.betfair.video.api.infra.output.adapter.GeolocationAdapter;
import com.betfair.video.api.infra.output.adapter.SuspectNetworkAdapter;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GeolocationAdapter Tests")
class GeolocationAdapterTest {

    @Mock
    private DatabaseReader cityReader;

    @Mock
    private SuspectNetworkAdapter suspectNetworkAdapter;

    @InjectMocks
    private GeolocationAdapter geolocationAdapter;

    private static final String VALID_IP = "192.168.0.1";

    @Test
    @DisplayName("Should return default geolocation for valid IP address")
    void shouldReturnDefaultGeolocationForValidIPAddress() throws IOException, GeoIp2Exception {
        // Given
        RequestContext requestContext = new RequestContext("uuid", List.of(VALID_IP));

        CityResponse mockCityResponse = mock(CityResponse.class);
        Country mockCountry = mock(Country.class);
        Subdivision mockSubdivision = mock(Subdivision.class);
        Location mockLocation = mock(Location.class);

        when(mockCountry.getIsoCode()).thenReturn("US");
        when(mockCityResponse.getCountry()).thenReturn(mockCountry);

        when(mockSubdivision.getIsoCode()).thenReturn("CA");
        when(mockCityResponse.getSubdivisions()).thenReturn(List.of(mockSubdivision));

        when(mockLocation.getMetroCode()).thenReturn(807);
        when(mockCityResponse.getLocation()).thenReturn(mockLocation);

        when(mockCityResponse.getTraits()).thenReturn(null);

        when(cityReader.tryCity(any(InetAddress.class))).thenReturn(Optional.of(mockCityResponse));
        when(suspectNetworkAdapter.isSuspectNetwork(VALID_IP)).thenReturn(false);

        // When
        Geolocation geolocation = geolocationAdapter.getUserGeolocation(requestContext);

        // Then
        assertThat(geolocation).isNotNull();
        assertThat(geolocation.countryCode()).isEqualTo("US");
        assertThat(geolocation.subDivisionCode()).isEqualTo("US-CA");
        assertThat(geolocation.dmaId()).isEqualTo(807);
        assertThat(geolocation.isSuspect()).isFalse();
    }

}
