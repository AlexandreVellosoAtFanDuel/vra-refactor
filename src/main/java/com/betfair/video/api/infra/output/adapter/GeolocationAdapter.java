package com.betfair.video.api.infra.output.adapter;

import com.betfair.video.api.domain.entity.RequestContext;
import com.betfair.video.api.domain.port.GeolocationPort;
import com.betfair.video.api.domain.port.SuspectNetworkPort;
import com.betfair.video.api.domain.valueobject.CountryAndSubdivisions;
import com.betfair.video.api.domain.valueobject.Geolocation;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AbstractCityResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class GeolocationAdapter implements GeolocationPort {

    private static final Logger logger = LoggerFactory.getLogger(GeolocationAdapter.class);

    private final DatabaseReader cityReader;

    private final SuspectNetworkPort suspectNetworkPort;

    public GeolocationAdapter(DatabaseReader cityReader, SuspectNetworkAdapter suspectNetworkPort) {
        this.cityReader = cityReader;
        this.suspectNetworkPort = suspectNetworkPort;
    }

    @Override
    public Geolocation getUserGeolocation(RequestContext userContext) {
        String clientIp = getUserIpAddress(userContext);

        Integer metroCode = resolveDmaId(userContext)
                .orElse(null);

        try {
            Validate.isTrue(StringUtils.isNotBlank(clientIp));
            InetAddress clientAddress = InetAddress.getByName(clientIp);
            CountryAndSubdivisions car = this.getCountryAndSubdivisions(clientAddress, cityReader);
            boolean isSuspect = suspectNetworkPort.isSuspectNetwork(clientIp);

            return new Geolocation(car.countryCode(), car.getRegionCode(), metroCode, isSuspect);
        } catch (GeoIp2Exception | IllegalArgumentException | IOException ex) {
            logger.warn("Failed querying MaxMind GeoIp 2 database for ip {}. Falling back to default - unknown geo details", clientIp, ex);
            return new Geolocation("--", null, metroCode, false);
        }
    }

    private CountryAndSubdivisions getCountryAndSubdivisions(InetAddress ip, DatabaseReader databaseReader) throws IOException, GeoIp2Exception {
        Optional<CityResponse> cityResponse = databaseReader.tryCity(ip);

        if (cityResponse.isEmpty()) {
            logger.warn("No city response found for IP address: {}", ip);
            throw new RuntimeException("Error when reading city response for IP address: " + ip);
        }

        String countryCode = Optional.ofNullable(cityResponse.get().getCountry())
                .map(Country::getIsoCode)
                .orElse("--");

        List<String> subdivisionsCodes = cityResponse.get()
                .getSubdivisions()
                .stream()
                .map(Subdivision::getIsoCode)
                .toList();

        Map<String, String> metaAttributes = Optional.ofNullable(cityResponse.get().getTraits())
                .map(this::extractMetaAttributes)
                .orElse(new HashMap<>());

        return new CountryAndSubdivisions(
                countryCode,
                subdivisionsCodes,
                metaAttributes
        );
    }

    private Optional<Integer> resolveDmaId(RequestContext userContext) {
        String clientIp = getUserIpAddress(userContext);

        Optional<Integer> dmaId = Optional.empty();
        try {
            InetAddress inetAddress = InetAddress.getByName(clientIp);
            dmaId = cityReader.tryCity(inetAddress)
                    .map(AbstractCityResponse::getLocation)
                    .map(Location::getMetroCode);

        } catch (IOException e) {
            logger.error("[{}]: Exception occurred when handling user address {}, {}}", userContext.uuid(), clientIp, e.getMessage());
        } catch (GeoIp2Exception e) {
            logger.error("[{}]: Exception occurred while retrieving geolocation data for user address {}, {}", userContext.uuid(), clientIp, e.getMessage());
        }

        return dmaId;
    }

    private String getUserIpAddress(RequestContext user) {
        return Optional.ofNullable(user.resolvedIps())
                .map(Collection::stream)
                .map(Stream::findFirst)
                .flatMap(firstIp -> firstIp)
                .orElse(null);
    }

    private Map<String, String> extractMetaAttributes(Traits traits) {
        Map<String, String> meta = new HashMap<>();
        meta.put("Trait.AutonomousSystemOrganization", traits.getAutonomousSystemOrganization());
        meta.put("Trait.Domain", traits.getDomain());
        meta.put("Trait.Isp", traits.getIsp());
        meta.put("Trait.MobileCountryCode", traits.getMobileCountryCode());
        meta.put("Trait.MobileNetworkCode", traits.getMobileNetworkCode());
        meta.put("Trait.Organization", traits.getOrganization());
        meta.put("Trait.UserType", traits.getUserType());
        meta.put("Trait.AutonomousSystemNumber", Optional.ofNullable(traits.getAutonomousSystemNumber()).map(Object::toString).orElse(null));
        meta.put("Trait.ConnectionType", Optional.ofNullable(traits.getConnectionType()).map(Enum::name).orElse(null));
        meta.put("Trait.StaticIpScore", Optional.ofNullable(traits.getStaticIpScore()).map(Object::toString).orElse(null));
        meta.put("Trait.UserCount", Optional.ofNullable(traits.getUserCount()).map(Object::toString).orElse(null));
        meta.put("Trait.IsAnonymous", Boolean.toString(traits.isAnonymous()));
        meta.put("Trait.IsAnonymousProxy", Boolean.toString(traits.isAnonymousProxy()));
        meta.put("Trait.IsAnonymousVpn", Boolean.toString(traits.isAnonymousVpn()));
        meta.put("Trait.IsHostingProvider", Boolean.toString(traits.isHostingProvider()));
        meta.put("Trait.IsLegitimateProxy", Boolean.toString(traits.isLegitimateProxy()));
        meta.put("Trait.IsPublicProxy", Boolean.toString(traits.isPublicProxy()));
        meta.put("Trait.IsResidentialProxy", Boolean.toString(traits.isResidentialProxy()));
        meta.put("Trait.IsSatelliteProvider", Boolean.toString(traits.isSatelliteProvider()));
        meta.put("Trait.IsTorExitNode", Boolean.toString(traits.isTorExitNode()));
        return meta;
    }

}
