package com.betfair.video.api.output.config;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
public class CityReaderBean {

    private static final Logger logger = LoggerFactory.getLogger(CityReaderBean.class);

    @Value("${geoip.database.file-path}")
    private String geoipDatabaseFilePath;

    @Bean
    public DatabaseReader loadDbReader() throws IOException, URISyntaxException {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource(geoipDatabaseFilePath);

            if (resourceUrl == null) {
                throw new IOException("GeoIP2-City file not found in classpath: " + geoipDatabaseFilePath);
            }

            URI uri = resourceUrl.toURI();
            File dbFile = new File(uri);
            FileInputStream fileInputStream = new FileInputStream(dbFile);

            logger.info("GeoIP2-City database file loaded successfully from: {} (configured path: {})",
                       dbFile.getAbsolutePath(), geoipDatabaseFilePath);

            return loadDbReader(fileInputStream);

        } catch (URISyntaxException e) {
            logger.error("Error converting resource URL to URI for GeoIP2-City", e);
            throw e;
        } catch (IOException e) {
            logger.error("Error loading GeoIP2-City database file", e);
            throw e;
        }
    }

    private DatabaseReader loadDbReader(InputStream inputStream) throws IOException {
        return new DatabaseReader.Builder(inputStream)
                .fileMode(Reader.FileMode.MEMORY)
                .withCache(new CHMCache())
                .build();
    }

}
